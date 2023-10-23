import { useSyncExternalStore, useState, useEffect } from "react";
import { Outlet } from "react-router-dom";
import { Box } from "@mui/material";
import AppLayout from "./layouts/AppLayout";
import GlobalCss from "./components/GlobalCss";
import LoginForm from "./components/LoginForm";
import SideBar from "./components/SideBar";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Loading from "./components/Loading";
import { navMaker } from "./routes";
import { userState } from "./stores/userState";
import axios from "axios";

function App() {
  const user = useSyncExternalStore(userState.subscribe, userState.get);
  const [loading, setLoading] = useState(true);
  const [drawer, setDrawer] = useState(false);

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

  const toggleDrawer = () => {
    setDrawer(!drawer);
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

  const routes = navMaker();
  return (
    <>
      <GlobalCss />
      <AppLayout
        sidebar={
          <SideBar open={drawer} onClose={toggleDrawer} routes={routes} />
        }
        header={
          <Header
            handleDrawerToggle={toggleDrawer}
            logoutHandler={handleLogout}
          />
        }
        content={loader}
        footer={<Footer />}
      />
    </>
  );
}

export default App;
