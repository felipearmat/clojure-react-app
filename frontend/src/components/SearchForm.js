import React, { useState } from "react";
import {
  Button,
  TextField,
  Container,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Grid,
} from "@mui/material";
import { styled } from "@mui/system";

const SearchFormContainer = styled(Container)(({ theme }) => ({
  paddingTop: theme.spacing(4),
}));

const SearchForm = ({ searchCallBack }) => {
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

  return (
    <SearchFormContainer>
      <form>
        <Grid container mt={3} mb={4} spacing={1}>
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
          <Grid item xs={12} sm={6}>
            <TextField
              name="operationCost"
              label="Operation Cost"
              fullWidth
              variant="outlined"
              value={searchParams.operationCost}
              onChange={handleInputChange}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth variant="outlined">
              <InputLabel id="amount-operator-label">Amount</InputLabel>
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
              variant="outlined"
              value={searchParams.amountValue}
              onChange={handleInputChange}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              name="startDate"
              label="Start Date"
              fullWidth
              variant="outlined"
              type="date"
              InputLabelProps={{
                shrink: true,
              }}
              value={searchParams.startDate}
              onChange={handleInputChange}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              name="endDate"
              label="End Date"
              fullWidth
              variant="outlined"
              type="date"
              InputLabelProps={{
                shrink: true,
              }}
              value={searchParams.endDate}
              onChange={handleInputChange}
            />
          </Grid>
        </Grid>
        <Box mt={2} textAlign="center">
          <Button
            variant="contained"
            color="primary"
            onClick={searchCallBack(searchParams)}
          >
            Search
          </Button>
        </Box>
      </form>
    </SearchFormContainer>
  );
};

export default SearchForm;
