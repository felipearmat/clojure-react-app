import React from "react";
import { Container, Grid, Box } from "@mui/material";
import { styled } from "@mui/system";

const Header = styled(Container)(({ theme }) => ({
  padding: "0",
  paddingLeft: "2rem",
  paddingRight: "2rem",
}));

const Footer = styled(Container)(({ theme }) => ({
  padding: "0.875rem",
  paddingLeft: "2rem",
  paddingRight: "2rem",
}));

const Content = styled(Container)(({ theme }) => ({
  padding: "0",
  flex: "1",
  display: "flex",
  flexDirection: "column",
  justifyContent: "center",
  alignItems: "center",
  height: "100%",
}));

const AppLayout = ({ sidebar, header, content, footer }) => {
  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      textAlign="center"
      height="100vh"
    >
      {sidebar && <div id="app_sidebar">{sidebar}</div>}
      {header && (
        <Header id="app_header" maxWidth="lg">
          {header}
        </Header>
      )}
      <Content id="app_content">
        <Grid container>
          <Grid item xs={12} sm={2} md={3} />
          <Grid item xs={12} sm={8} md={6}>
            {content}
          </Grid>
          <Grid item xs={12} sm={2} md={3} />
        </Grid>
      </Content>
      {footer && (
        <Footer id="app_footer" maxWidth="lg">
          {footer}
        </Footer>
      )}
    </Box>
  );
};

export default AppLayout;
