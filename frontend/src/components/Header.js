import { AppBar, Toolbar, IconButton, Typography } from "@mui/material";
import { ExitToApp } from "@mui/icons-material";
import { styled } from "@mui/system";
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
  fontSize: "1.5rem",
}));

const StyledLogoutButton = styled(IconButton)(({ theme }) => ({
  color: "white",
}));

function Header({ isLoggedIn, logoutHandler }) {
  const handleLogout = async () => {
    try {
      await axios.post("http://localhost/api/logout", {
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
        <StyledTypography variant="h6">
          ArithmeticCalculatorAPI
        </StyledTypography>
        {isLoggedIn && (
          <StyledLogoutButton onClick={handleLogout}>
            <ExitToApp />
          </StyledLogoutButton>
        )}
      </StyledToolbar>
    </StyledAppBar>
  );
}

export default Header;
