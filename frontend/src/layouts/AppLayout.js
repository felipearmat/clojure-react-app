import React from "react";
import { Container } from "@mui/material";
import { styled } from "@mui/system";

const LayoutContainer = styled(Container)(({ theme }) => ({
  display: "flex",
  flexDirection: "column",
  minHeight: "100vh",
  padding: theme?.spacing(0),
}));

const Header = styled(Container)(({ theme }) => ({
  backgroundColor: theme?.palette?.primary?.main,
  color: "#fff",
  padding: theme?.spacing(0),
}));

const Footer = styled(Container)(({ theme }) => ({
  backgroundColor: theme?.palette?.primary?.main,
  color: "#fff",
  padding: theme?.spacing(0),
}));

const Content = styled(Container)(({ theme }) => ({
  display: "flex",
  flexDirection: "column",
  justifyContent: "center",
  alignItems: "center",
  height: "100%",
  padding: theme?.spacing(0),
  flex: 1,
}));

const AppLayout = ({ sidebar, header, content, footer }) => {
  return (
    <LayoutContainer>
      {sidebar && <div id="app_sidebar">{sidebar}</div>}
      {header && <Header id="app_header">{header}</Header>}
      <Content id="app_content">{content}</Content>
      {footer && <Footer id="app_footer">{footer}</Footer>}
    </LayoutContainer>
  );
};

export default AppLayout;
