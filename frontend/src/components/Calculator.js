import { Button, TextField, Typography, Snackbar, Alert } from "@mui/material";
import { styled } from "@mui/material/styles";
import { userState } from "../stores/userState";
import { useState } from "react";
import axios from "axios";
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

  const handleRequest = async (expression) => {
    if (!expression) return;
    setLoading(true);

    try {
      const response = await axios.post("/api/v1/calculate", { expression });
      const data = response.data;
      const newHistory = `${expression} = ${data.result}`;
      userState.set({
        balance: data.balance,
      });
      setExpression([]);
      setHistory((prevHistory) => [...prevHistory, newHistory].slice(-10));
    } catch (error) {
      setError(error?.response?.data || error.message);
    } finally {
      setLoading(false);
    }
  };

  const readExpression = (expression) => {
    return (Array.isArray(expression) && expression.join("")) || "";
  };

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

  return (
    <div>
      <Loading isLoading={loading}>
        <h4 identificator="calculator-history-title">History:</h4>
        {history.map((item, index) => (
          <Typography
            identificator={`calculator-history-item-${index}`}
            key={`history${index}`}
          >
            {item}
          </Typography>
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
            <div key={`row${index}`} style={{ display: "flex" }}>
              {buttonRow.map((label) => (
                <StyledButton
                  key={`label${label}`}
                  identificator={`calculator-button-${label}`}
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
          identificator="calculator-error-snackbar"
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
