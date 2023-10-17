import Calculator from "../components/Calculator";
import { styled } from "@mui/material/styles";
import { Container } from "@mui/material";

const StyledContainer = styled(Container)`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  text-align: left;
`;

const CalculatorView = () => {
  return (
    <StyledContainer>
      <Calculator />
    </StyledContainer>
  );
};

export default CalculatorView;
