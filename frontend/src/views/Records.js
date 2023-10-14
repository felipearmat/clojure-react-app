import { useState, useEffect } from "react";
import axios from "axios";
import { Container, Typography } from "@mui/material";
import DataTable from "../components/DataTable";

const RecordList = () => {
  const [records, setRecords] = useState([]);
  const [totalBalance, setTotalBalance] = useState(0);

  useEffect(() => {
    // Make an HTTP request to get the user's records
    axios
      .get("http://localhost/api/records")
      .then((response) => {
        const data = response.data;
        if (Array.isArray(data)) {
          setRecords(data);
          calculateTotalBalance(data);
        }
      })
      .catch((error) => {
        console.error("Error fetching records:", error);
      });
  }, []);

  const calculateTotalBalance = (data) => {
    const total = data.reduce((acc, record) => acc + record.amount, 0);
    setTotalBalance(total);
  };

  const columns = [
    {
      id: "id",
      label: "ID",
    },
    {
      id: "operation_id",
      label: "Operation ID",
    },
    {
      id: "amount",
      label: "Amount",
    },
    {
      id: "user_balance",
      label: "User Balance",
    },
  ];

  return (
    <Container>
      <DataTable
        title="Banana"
        rows={records}
        columns={columns}
        pagination={true}
      />
      <Typography variant="h6" gutterBottom>
        Total Balance: {totalBalance}
      </Typography>
    </Container>
  );
};

export default RecordList;
