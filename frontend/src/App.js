import { useSyncExternalStore, useState, useEffect } from "react";
import { Outlet } from "react-router-dom";
import { Box } from "@mui/material";
import AppLayout from "./layouts/AppLayout";
import GlobalCss from "./components/GlobalCss";
import LoginForm from "./components/LoginForm";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Loading from "./components/Loading";
import { userState } from "./stores/userState";
import axios from "axios";

function App() {
  const [loading, setLoading] = useState(true);
  const user = useSyncExternalStore(userState.subscribe, userState.get);

  const fetchUserData = async () => {
    try {
      const response = await axios.get("/api/data");
      const data = response.data;
      if (data.logged === true) {
        userState.set({
          authenticated: data.logged,
          balance: data.balance,
          email: data.email,
        });
      }
    } catch (error) {
      console.error("Error checking authentication:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUserData();
  }, []);

  const authCallBack = () => {
    fetchUserData();
  };

  const handleLogout = () => {
    userState.set({ authenticated: false, balance: null, email: null });
  };

  const loader = user.authenticated ? (
    <Box width="100%" identificator="app-outlet">
      <Loading isLoading={loading}>
        <Outlet />
      </Loading>
    </Box>
  ) : (
    <Box width="100%" identificator="app-login-form">
      <LoginForm authCallBack={authCallBack} />
    </Box>
  );

  return (
    <>
      <GlobalCss />
      <AppLayout
        header={<Header logoutHandler={handleLogout} />}
        content={loader}
        footer={<Footer />}
      />
    </>
  );
}

export default App;
