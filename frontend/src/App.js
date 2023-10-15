import { useState, useEffect } from "react";
import { Outlet } from "react-router-dom";
import AppLayout from "./layouts/AppLayout";
import GlobalCss from "./components/GlobalCss";
import LoginForm from "./components/LoginForm";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Loading from "./components/Loading";
import axios from "axios";

function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  const handleLogout = () => {
    setAuthenticated(false);
  };

  const loader = authenticated ? (
    <Loading isLoading={loading}>
      <Outlet />
    </Loading>
  ) : (
    <LoginForm setAuthenticated={setAuthenticated} />
  );

  useEffect(() => {
    const checkAuthentication = async () => {
      try {
        const response = await axios.get("http://localhost/api/logged");
        const data = response.data;
        if (data.authenticated === true) {
          setAuthenticated(true);
        }
        setLoading(false);
      } catch (error) {
        console.error("Error checking authentication:", error);
        setLoading(false);
      }
    };

    checkAuthentication();
  }, []);

  return (
    <>
      <GlobalCss />
      <AppLayout
        header={
          <Header isLoggedIn={authenticated} logoutHandler={handleLogout} />
        }
        content={loader}
        footer={<Footer />}
      />
    </>
  );
}

export default App;
