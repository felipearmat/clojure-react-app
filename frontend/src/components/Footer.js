import { Typography } from "@mui/material";
import { styled } from "@mui/system";

const StyledTypography = styled(Typography)(({ theme }) => ({
  padding: 0,
  marginTop: "auto",
}));

function Footer() {
  return (
    <StyledTypography variant="body2">
      Your Footer Information Here
    </StyledTypography>
  );
}

export default Footer;
