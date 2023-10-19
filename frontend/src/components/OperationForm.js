import { useState } from "react";
import axios from "axios";
import { Button, TextField, Container, Grid, Typography } from "@mui/material";
import { styled } from "@mui/system";

const FormContainer = styled(Container)({
  padding: "16px",
  maxWidth: "400px",
  margin: "0 auto",
});

const FormTitle = styled(Typography)({
  fontSize: "1.5rem",
  fontWeight: "bold",
  marginBottom: "16px",
});

const FormButton = styled(Button)({
  marginTop: "16px",
});

function OperationForm() {
  const [operationType, setOperationType] = useState("");
  const [cost, setCost] = useState("");

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    try {
      await axios.post("/api/operations", {
        type: operationType,
        cost: parseFloat(cost),
      });

      setOperationType("");
      setCost("");
    } catch (error) {
      console.error("Error creating operation:", error);
    }
  };

  return (
    <FormContainer>
      <FormTitle>Create Operation</FormTitle>
      <form onSubmit={handleFormSubmit}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <TextField
              label="Operation Type"
              variant="outlined"
              fullWidth
              value={operationType}
              onChange={(e) => setOperationType(e.target.value)}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              label="Cost"
              variant="outlined"
              type="number"
              fullWidth
              value={cost}
              onChange={(e) => setCost(e.target.value)}
            />
          </Grid>
          <Grid item xs={12}>
            <FormButton
              type="submit"
              variant="contained"
              color="primary"
              fullWidth
            >
              Create Operation
            </FormButton>
          </Grid>
        </Grid>
      </form>
    </FormContainer>
  );
}

export default OperationForm;
