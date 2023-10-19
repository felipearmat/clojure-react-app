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

  const handleLogout = () => {
    userState.set({
      authenticated: false,
    });
  };

  const loader = user.authenticated ? (
    <Loading isLoading={loading}>
      <Outlet />
    </Loading>
  ) : (
    <LoginForm />
  );

  useEffect(() => {
    const checkAuthentication = async () => {
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
          <Header
            isLoggedIn={user.authenticated}
            data={user}
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
