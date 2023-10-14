import React, { useState, useEffect } from "react";
import axios from "axios";
import Container from "@mui/material/Container";
import Typography from "@mui/material/Typography";
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableBody from "@mui/material/TableBody";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";

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

  return (
    <Container>
      <Typography variant="h4" gutterBottom>
        My Records
      </Typography>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Operation ID</TableCell>
              <TableCell>Amount</TableCell>
              <TableCell>User Balance</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {records.map((record) => (
              <TableRow key={record.id}>
                <TableCell>{record.id}</TableCell>
                <TableCell>{record.operation_id}</TableCell>
                <TableCell>{record.amount}</TableCell>
                <TableCell>{record.user_balance}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Typography variant="h6" gutterBottom>
        Total Balance: {totalBalance}
      </Typography>
    </Container>
  );
};

export default RecordList;
