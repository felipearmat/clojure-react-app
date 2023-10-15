import React, { useState, useEffect } from "react";
import axios from "axios";
import { Container, Typography } from "@mui/material";
import DataTable from "../components/DataTable";

const RecordList = () => {
  const [records, setRecords] = useState([]);
  const [totalAmount, setTotalAmount] = useState(0);

  useEffect(() => {
    axios
      .get("http://localhost/api/v1/records")
      .then((response) => {
        const data = response.data;
        console.info("sample:", response.data[0]);
        if (Array.isArray(data)) {
          setRecords(data);
          calculateTotalAmount(data);
        }
      })
      .catch((error) => {
        console.error("Error fetching records:", error);
      });
  }, []);

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
    {
      id: "user_balance",
      label: "User Balance",
      format: (val) => Number.parseFloat(val).toFixed(2),
    },
  ];

  return (
    <Container>
      {records.length > 0 ? (
        <>
          <DataTable
            title="Records"
            rows={records}
            columns={headers}
            pagination={true}
          />
          <Typography variant="h6" gutterBottom>
            Total Amount: {totalAmount}
          </Typography>
        </>
      ) : (
        <Typography variant="h6" gutterBottom>
          No records available.
        </Typography>
      )}
    </Container>
  );
};

export default RecordList;
