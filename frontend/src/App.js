import { useState, useEffect } from "react";
import { Outlet } from "react-router-dom";
import GlobalCss from "./components/GlobalCss";
import LoginForm from "./components/LoginForm";
import Header from "./components/Header";
import AppLayout from "./layouts/AppLayout";
import { css } from "@emotion/react";
import { Container, CircularProgress } from "@mui/material";
import axios from "axios";

const classes = {
  root: css`
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    height: "100vh",
  `,
};

function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  const handleLogout = () => {
    setAuthenticated(false);
  };

  const loader = function () {
    return loading ? (
      <CircularProgress />
    ) : authenticated ? (
      <Outlet setAuthenticated={setAuthenticated} />
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
  }, []);

  return (
    <Container className={classes.root}>
      <GlobalCss />
      <AppLayout
        header={<Header logoutHandler={handleLogout} />}
        content={loader()}
        // footer={<Footer />}
        footer={<div> Footer </div>}
      />
    </Container>
  );
}

export default App;
