import { useSyncExternalStore, useState, useEffect } from "react";
import { Outlet } from "react-router-dom";
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
    userState.set({});
  };

  const loader = user.authenticated ? (
    <Loading isLoading={loading}>
      <Outlet />
    </Loading>
  ) : (
    <LoginForm id="app_login_form" authCallBack={authCallBack} />
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
