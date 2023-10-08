import { useState } from "react";
import GlobalCss from "./components/GlobalCss";
import LoginForm from "./components/LoginForm";
import LogoSVG from "./components/logoSVG";
import { Container, Typography, Button, Box } from "@mui/material";

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
      <GlobalCss />
      <div>
        {isLoggedIn ? (
          <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            textAlign="center"
            height="100vh"
          >
            <Typography variant="h4">You're logged in!</Typography>
            <Box mt={2}>
              <Button
                variant="contained"
                color="primary"
                onClick={handleLogout}
              >
                Logout
              </Button>
            </Box>
          </Box>
        ) : (
          <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            textAlign="center"
            height="100vh"
          >
            <Box mb={1}>
              <LogoSVG />
            </Box>
            <LoginForm onLogin={handleLogin} />
          </Box>
        )}
      </div>
    </Container>
  );
}

export default App;
