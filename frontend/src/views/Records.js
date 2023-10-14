import React, { useState, useEffect } from "react";
import axios from "axios";
import { Container, Typography } from "@mui/material";
import DataTable from "../components/DataTable";

const RecordList = () => {
  const [records, setRecords] = useState([]);
  const [totalBalance, setTotalBalance] = useState(0);

  useEffect(() => {
    axios
      .get("http://localhost/api/v1/records")
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

  const headers = [
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
      {records.length > 0 ? (
        <>
          <DataTable
            title="Records"
            rows={records}
            columns={headers}
            pagination={true}
          />
          <Typography variant="h6" gutterBottom>
            Total Balance: {totalBalance}
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
