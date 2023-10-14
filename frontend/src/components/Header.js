import React from "react";
import { AppBar, Button } from "@mui/material";
import { css } from "@emotion/react";
import axios from "axios";

const classes = {
  app: css`
    display: "flex",
    flexDirection: "column",
    minHeight: "100vh",
  `,
  header: css`
    padding: "16px";
  `,
  footer: css`
    padding: "16px",
    marginTop: "auto",
  `,
  content: css`
    flexGrow: 1,
    padding: "16px",
  `,
  formPaper: css`
    padding: "16px",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  `,
  logoutButton: css`
    marginLeft: "auto",
    color: "white",
    backgroundColor: "#f44336", // Red color
  `,
};

function Header({ logoutHandler }) {
  const logout = async (e) => {
    e.preventDefault();
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
    <AppBar position="static" className={classes.header}>
      <Button className={classes.logoutButton} onClick={logout}>
        Logout
      </Button>
    </AppBar>
  );
}
export default Header;
