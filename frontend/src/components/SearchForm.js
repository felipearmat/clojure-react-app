import {
  Box,
  Button,
  Container,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import { styled } from "@mui/system";
import { useState } from "react";

const SearchFormContainer = styled(Container)(({ theme }) => ({
  paddingTop: theme.spacing(4),
}));

const StyledButton = styled(Button)({
  margin: "0 0.75rem",
});

const SearchForm = ({ searchCallBack, children }) => {
  const [searchParams, setSearchParams] = useState({
    operationType: "",
    operationCost: "",
    amountOperator: "",
    amountValue: "",
    startDate: "",
    endDate: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setSearchParams({ ...searchParams, [name]: value });
  };

  const filteredParams = () => {
    const result = {};
    for (const [key, value] of Object.entries(searchParams)) {
      if (value) result[key] = value;
    }
    return Object.keys(result).length > 0 && result;
  };

  return (
    <SearchFormContainer>
      <form>
        <Grid container mt={3} mb={4} spacing={1}>
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth variant="outlined">
              <InputLabel id="amount-operator-label">Amount is</InputLabel>
              <Select
                name="amountOperator"
                labelId="amount-operator-label"
                value={searchParams.amountOperator}
                onChange={handleInputChange}
                label="Amount"
              >
                <MenuItem value="">Select</MenuItem>
                <MenuItem value="<">Less Than</MenuItem>
                <MenuItem value="=">Equal To</MenuItem>
                <MenuItem value=">">Greater Than</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              name="amountValue"
              label="Amount Value"
              fullWidth
              type="number"
              variant="outlined"
              value={searchParams.amountValue}
              onChange={handleInputChange}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth variant="outlined">
              <InputLabel id="operation-type-label">Operation Type</InputLabel>
              <Select
                name="operationType"
                labelId="operation-type-label"
                value={searchParams.operationType}
                onChange={handleInputChange}
                label="Operation Type"
              >
                <MenuItem value="">Select</MenuItem>
                <MenuItem value="addition">Addition</MenuItem>
                <MenuItem value="subtraction">Subtraction</MenuItem>
                <MenuItem value="multiplication">Multiplication</MenuItem>
                <MenuItem value="division">Division</MenuItem>
                <MenuItem value="square_root">Square Root</MenuItem>
                <MenuItem value="random_string">Random String</MenuItem>
              </Select>
            </FormControl>
          </Grid>
        </Grid>
        <Box mt={2} textAlign="center">
          <StyledButton
            variant="contained"
            color="primary"
            onClick={() => searchCallBack(filteredParams())}
          >
            Search
          </StyledButton>
          {children}
        </Box>
      </form>
    </SearchFormContainer>
  );
};

export default SearchForm;
