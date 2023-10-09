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

const LoginForm = ({ onLogin }) => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    token: "",
  });
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit1 = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      const response = await axios.post(
        "http://172.16.238.10:3000/api/login",
        formData,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      setFormData({
        ...formData,
        token: response.data.token,
      });
      console.log("Logged in:", response);
      // onLogin();
    } catch (err) {
      setError("Invalid credentials. Please try again.");
    }
  };

  const handleSubmit2 = async (e) => {
    e.preventDefault();
    setError(null);

    let data = JSON.stringify(formData);

    try {
      const response = await axios.post(
        "http://172.16.238.10:3000/api/v1/restricted",
        data,
        {
          headers: {
            Authorization: `Token ${formData["token"]}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log("Restricted data:", response.data);
    } catch (err) {
      setError("Invalid credentials. Please try again.");
    }
  };

  return (
    <StyledPaper elevation={3}>
      <Typography variant="h4">Sign in</Typography>
      <form onSubmit={handleSubmit1}>
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
      <form onSubmit={handleSubmit2}>
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
