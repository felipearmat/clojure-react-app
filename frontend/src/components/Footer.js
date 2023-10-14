import { Typography, Container } from "@mui/material";
import { styled } from "@mui/system";

const StyledFooter = styled(Container)(({ theme }) => ({
  backgroundColor: theme?.palette?.primary?.main,
  color: theme?.palette?.primary?.contrastText,
  padding: "1rem",
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  minHeight: "3rem", // Specify the desired height
}));

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
