import React, { useState } from "react";
import axios from "axios";
import { Button, TextField, Typography, Paper, Box } from "@mui/material";
import { styled } from "@mui/system";

const StyledPaper = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(1.5),
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  gap: theme.spacing(1.5),
  maxWidth: "300px",
  margin: "0 auto",
}));

const StyledButton = styled(Button)(() => ({
  backgroundColor: "#4285F4",
  color: "#fff",
  "&:hover": {
    backgroundColor: "#3579D8",
  },
  margin: "1rem 0 0.5rem 0",
}));

const LoginForm = ({ setAuthenticated }) => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      await axios.post("http://localhost/api/login", formData, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      setAuthenticated(true);
    } catch (err) {
      console.log(err);
      setError("Invalid credentials. Please try again.");
    }
  };

  return (
    <StyledPaper elevation={3}>
      <Typography variant="h4">Sign in</Typography>
      <form onSubmit={handleSubmit}>
        <TextField
          label="Email"
          name="username"
          value={formData.username}
          onChange={handleChange}
          margin="normal"
          fullWidth
          required
        />
        <TextField
          type="password"
          label="Password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          margin="normal"
          fullWidth
          required
        />
        {error && (
          <Box mt={1}>
            <Typography color="error">{error}</Typography>
          </Box>
        )}
        <StyledButton type="submit" variant="contained">
          Submit
        </StyledButton>
      </form>
    </StyledPaper>
  );
};

export default LoginForm;
