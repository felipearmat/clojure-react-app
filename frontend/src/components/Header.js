import { AppBar, IconButton, Toolbar, Typography } from "@mui/material";
import { ExitToApp, MenuOpen } from "@mui/icons-material";
import { styled } from "@mui/system";
import { userState } from "../stores/userState";
import { useSyncExternalStore } from "react";
import axios from "axios";

const StyledAppBar = styled(AppBar)(({ theme }) => ({
  backgroundColor: "#1976D2",
}));

const StyledToolbar = styled(Toolbar)({
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  padding: "0.5rem 1rem",
});

const StyledTypography = styled(Typography)(({ theme }) => ({
  color: "white",
  fontWeight: "bold",
  fontSize: "1.25rem",
}));

const Data = styled(Typography)(({ theme }) => ({
  color: "white",
  fontWeight: "bold",
  fontSize: "1rem",
  textAlign: "right",
  width: "100%",
  padding: "0 2.5rem",
  whiteSpace: "pre-wrap",
}));

const StyledLogoutButton = styled(IconButton)(({ theme }) => ({
  color: "white",
}));

const userInfo = (email, balance) => {
  return `${email}   /   Balance: ${Number.parseFloat(balance).toFixed(2)}`;
};

const Header = ({ handleDrawerToggle, logoutHandler }) => {
  const user = useSyncExternalStore(userState.subscribe, userState.get);

  const handleLogout = async () => {
    try {
      await axios.post("/api/logout", {
        headers: {
          "Content-Type": "application/json",
        },
      });
      logoutHandler();
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <StyledAppBar position="static">
      <StyledToolbar>
        <IconButton
          color="inherit"
          aria-label="open drawer"
          edge="start"
          onClick={handleDrawerToggle}
          sx={{ mr: 2, display: { sm: "none" } }}
        >
          <MenuOpen />
        </IconButton>
        <StyledTypography variant="h6">
          ArithmeticCalculatorAPI
        </StyledTypography>
        {user.authenticated && (
          <>
            {user.email && <Data>{userInfo(user.email, user.balance)}</Data>}
            <StyledLogoutButton onClick={handleLogout}>
              <ExitToApp />
            </StyledLogoutButton>
          </>
        )}
      </StyledToolbar>
    </StyledAppBar>
  );
};

export default Header;
