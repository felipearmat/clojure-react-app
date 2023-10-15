import { Typography, Container } from "@mui/material";
import { styled } from "@mui/system";

const StyledFooter = styled(Container)(({ theme }) => {
  const backgroundColor = theme?.palette?.primary?.main;
  const color = theme?.palette?.primary?.contrastText || "#000000";
  return {
    backgroundColor: backgroundColor,
    color: color,
    padding: "1rem",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    minHeight: "3rem",
  };
});

const StyledTypography = styled(Typography)(({ theme }) => ({
  margin: 0,
}));

function Footer() {
  return (
    <StyledFooter>
      <StyledTypography variant="body2">
        Your Footer Information Here
      </StyledTypography>
    </StyledFooter>
  );
}

export default Footer;
