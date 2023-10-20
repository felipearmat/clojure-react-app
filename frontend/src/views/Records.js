import { useState } from "react";
import { Container, Typography } from "@mui/material";
import DataTable from "../components/DataTable";
import SearchForm from "../components/SearchForm";
import axios from "axios";

const RecordList = () => {
  const [records, setRecords] = useState([]);
  const [totalAmount, setTotalAmount] = useState(0);

  const handleSearch = (params) => {
    axios
      .get("/api/v1/record", { params })
      .then((response) => {
        const data = response?.data?.records;
        console.log("Records", data);
        if (Array.isArray(data)) {
          setRecords(data);
          calculateTotalAmount(data);
        }
      })
      .catch((error) => {
        console.error("Error fetching record:", error);
      });
  };

  const calculateTotalAmount = (data) => {
    const total = data.reduce((acc, record) => acc + record.amount, 0);
    setTotalAmount(Number.parseFloat(total).toFixed(2));
  };

  const headers = [
    {
      id: "created_at",
      label: "Date",
    },
    {
      id: "operation_type",
      label: "Operation Type",
    },
    {
      id: "amount",
      label: "Amount",
      format: (val) => Number.parseFloat(val).toFixed(2),
    },
  ];

  return (
    <>
      <SearchForm searchCallBack={handleSearch} />
      <Container>
        {records.length > 0 ? (
          <>
            <div>
              <DataTable
                title="Records"
                rows={records}
                columns={headers}
                pageSizeOptions={[10, 25, 50]}
              />
            </div>
            <Typography>Total Amount: {totalAmount}</Typography>
          </>
        ) : (
          <Typography variant="h6" gutterBottom>
            No records available.
          </Typography>
        )}
      </Container>
    </>
  );
};

export default RecordList;
