import { useState, useEffect } from "react";
import axios from "axios";
import { styled } from "@mui/material/styles";
import { Button, TextField, Typography, Snackbar, Alert } from "@mui/material";
import { userState } from "../stores/userState";
import Loading from "./Loading";

const StyledInput = styled(TextField)`
  width: 100%;
  margin-bottom: 16px;
  text-align: left;
`;

const StyledButton = styled(Button)(({ _theme }) => ({
  margin: "4px",
  textAlign: "left",
  border: "1px solid #e0e0e0",
  borderRadius: "4px",
  padding: "8px 12px",
  "&:hover": {
    backgroundColor: "#DCEAF9",
  },
}));

const buttonArrays = [
  ["7", "8", "9", "/", "C", "<-"],
  ["4", "5", "6", "*", "(", ")"],
  ["1", "2", "3", "-", "√("],
  [".", "0", "=", "+", "randomstr"],
];

const Calculator = () => {
  const [expression, setExpression] = useState([]);
  const [history, setHistory] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setExpression([]);
  }, []);

  const handleInput = (symbol) => {
    switch (symbol) {
      case "C":
        setExpression([]);
        break;
      case "<-":
        setExpression((prevExpression) => prevExpression.slice(0, -1));
        break;
      case "randomstr":
        handleRequest("randomstr");
        break;
      case "=":
        handleRequest(readExpression(expression));
        break;
      default:
        setExpression((prevExpression) => [...prevExpression, symbol]);
    }
  };

  const readExpression = (expression) => {
    return (Array.isArray(expression) && expression.join("")) || "";
  };

  const handleRequest = (expression) => {
    if (!expression) return;
    setLoading(true);
    axios
      .post("/api/v1/calculate", { expression })
      .then((response) => {
        const data = response.data;
        const newHistory = `${expression} = ${data.result}`;
        userState.set({
          balance: data.balance,
        });
        setExpression([]);
        setHistory((prevHistory) => [...prevHistory, newHistory].slice(-10));
        setLoading(false);
      })
      .catch((error) => {
        setLoading(false);
        setError(error?.response?.data || error.message);
      });
  };

  return (
    <div>
      <Loading isLoading={loading}>
        <h4>History:</h4>
        {history.map((item, index) => (
          <Typography key={"history" + index}>{item}</Typography>
        ))}
        <form>
          <StyledInput
            value={readExpression(expression)}
            readOnly
            InputProps={{
              readOnly: true,
            }}
          />
          {buttonArrays.map((buttonRow, index) => (
            <div key={"row" + index} style={{ display: "flex" }}>
              {buttonRow.map((label) => (
                <StyledButton
                  key={"label" + label}
                  onClick={() => handleInput(label)}
                >
                  {label}
                </StyledButton>
              ))}
            </div>
          ))}
        </form>
      </Loading>
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
