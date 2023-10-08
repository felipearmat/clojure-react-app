import React, { useState } from "react";
import { Container, CssBaseline, Typography, Button } from "@mui/material";
import LoginForm from "./LoginForm";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleLogin = () => {
    // In a real application, you would perform authentication here.
    // For this example, we're assuming successful login when this function is called.
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    // In a real application, you would perform logout here.
    // For this example, we're simulating logout when this function is called.
    setIsLoggedIn(false);
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div>
        {isLoggedIn ? (
          <>
            <Typography variant="h4">You're logged in!</Typography>
            <Button variant="contained" color="primary" onClick={handleLogout}>
              Logout
            </Button>
          </>
        ) : (
          <>
            <Typography variant="h2">Welcome</Typography>
            <LoginForm onLogin={handleLogin} />
          </>
        )}
      </div>
    </Container>
  );
}

export default App;
