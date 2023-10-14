import { useState, useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import GlobalCss from "./components/GlobalCss";
import LoginForm from "./components/LoginForm";
import Header from "./components/Header";
import Footer from "./components/Footer";
import { styled } from "@mui/system";
import AppLayout from "./layouts/AppLayout";
import { Container, CircularProgress } from "@mui/material";
import axios from "axios";

const StyledContainer = styled(Container)(({ _theme }) => ({
  display: "block",
  height: "100vh",
}));

function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate(); // Get the navigation function

  const handleLogout = () => {
    setAuthenticated(false);
  };

  const loader = function () {
    return loading ? (
      <CircularProgress />
    ) : authenticated ? (
      <Outlet />
    ) : (
      <LoginForm setAuthenticated={setAuthenticated} />
    );
  };

  useEffect(() => {
    axios
      .get("http://localhost/api/logged")
      .then((response) => {
        const data = response.data;
        if (data.authenticated === true) {
          setAuthenticated(true);
        }
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error checking authentication:", error);
        setLoading(false);
      });
  }, [navigate]); // Pass navigate as a dependency

  return (
    <StyledContainer>
      <GlobalCss />
      <AppLayout
        header={
          <Header isLoggedIn={authenticated} logoutHandler={handleLogout} />
        }
        content={loader()}
        footer={<Footer />}
      />
    </StyledContainer>
  );
}

export default App;
