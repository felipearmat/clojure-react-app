import React, { useState, useEffect } from "react";
import axios from "axios";
import { styled } from "@mui/material/styles";
import { Button, TextField, Box, Snackbar, Alert } from "@mui/material";

const StyledInput = styled(TextField)`
  width: 100%;
  margin-bottom: 16px;
  text-align: left;
`;

const StyledButton = styled(Button)(({ theme }) => ({
  margin: "4px",
  textAlign: "left",
  border: "1px solid #e0e0e0" /* Add a thin gray border */,
  borderRadius: "4px" /* Round the corners of the border */,
  padding: "8px 12px" /* Add some padding to the button */,
  "&:hover": {
    backgroundColor: "#DCEAF9",
  },
}));

const StyledHistoryItem = styled(Box)`
  margin-bottom: 4px;
`;

const buttonArrays = [
  ["(", ")", "C", "√"],
  ["7", "8", "9", "/"],
  ["4", "5", "6", "*"],
  ["1", "2", "3", "-"],
  [".", "0", "="],
  ["randomstr"],
];

const Calculator = () => {
  const [expression, setExpression] = useState("");
  const [history, setHistory] = useState([]);
  const [result, setResult] = useState("");
  const [error, setError] = useState(null);

  const handleInput = (symbol) => {
    switch (symbol) {
      case "C":
        setExpression("");
        break;
      case "randomstr":
        handleRandomStr(expression);
        break;
      case "=":
        handleRequest(expression);
        break;
      default:
        setExpression((prevExpression) => prevExpression + symbol);
    }
  };

  const handleRequest = (expression) => {
    axios
      .post("/api/v1/calculate", { expression: expression })
      .then((response) => {
        setResult(response.data.result);
        updateHistory();
      })
      .catch((error) => {
        setError(error.message);
      });
  };

  const handleRandomStr = () => {
    handleRequest("randomstr");
  };

  const updateHistory = () => {
    const newHistory = `${expression} = ${result}`;
    setHistory((prevHistory) => [...prevHistory, ...newHistory].slice(-15));
  };

  useEffect(() => {
    setExpression("");
    setResult("");
  }, []);

  return (
    <div>
      <h4>History:</h4>
      {history.map((item, index) => (
        <StyledHistoryItem key={index}>{item}</StyledHistoryItem>
      ))}
      <StyledInput
        value={expression}
        readOnly
        InputProps={{
          readOnly: true,
        }}
      />

      {buttonArrays.map((buttonRow) => (
        <div style={{ display: "flex" }}>
          {buttonRow.map((buttonLabel) => (
            <StyledButton onClick={() => handleInput(buttonLabel)}>
              {buttonLabel}
            </StyledButton>
          ))}
        </div>
      ))}

      {error && (
        <Snackbar
          open={true}
          autoHideDuration={5000}
          onClose={() => setError(null)}
        >
          <Alert severity="error">{error}</Alert>
        </Snackbar>
      )}
    </div>
  );
};

export default Calculator;
