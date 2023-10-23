import {
  Alert,
  Button,
  Container,
  IconButton,
  Snackbar,
  Typography,
} from "@mui/material";
import { styled } from "@mui/system";
import { useGridApiRef } from "@mui/x-data-grid";
import { userState } from "../stores/userState";
import { useState } from "react";
import axios from "axios";
import DeleteIcon from "@mui/icons-material/Delete";
import SearchForm from "../components/SearchForm";
import XDataGrid from "../components/XDataGrid";

const StyledButton = styled(Button)({
  margin: "0 0.75rem",
});

const RecordList = () => {
  const [records, setRecords] = useState([]);
  const [totalAmount, setTotalAmount] = useState(0);
  const [alert, setAlert] = useState({ type: "error", message: "" });
  const [lastSearch, setLastSearch] = useState({});
  const apiRef = useGridApiRef();

  const getSelectedRows = (apiRef) => {
    const result = [];
    apiRef.current.getSelectedRows().forEach((value) => {
      result.push(value);
    });
    return result;
  };

  const handleSearch = async (params) => {
    setLastSearch(params);
    try {
      const response = await axios.get("/api/v1/record", { params });
      const data = response?.data?.records;
      if (Array.isArray(data)) {
        setRecords(data);
        calculateTotalAmount(data);
      }
    } catch (error) {
      setAlert({ type: "error", message: `Error fetching records: ${error}` });
    }
  };

  const calculateTotalAmount = (data) => {
    const total = data.reduce((acc, record) => acc + record.amount, 0);
    setTotalAmount(Number.parseFloat(total).toFixed(2));
  };

  const handleDelete = async (params) => {
    try {
      const response = await axios.post("/api/v1/record/delete", {
        records: params.map((record) => record.id),
      });
      const data = response?.data;
      setAlert({ type: "success", message: data?.message });
      userState.set({
        balance: data?.balance,
      });
      handleSearch(lastSearch);
    } catch (error) {
      setAlert({ type: "error", message: `Error deleting records: ${error}` });
    }
  };

  const formatDateString = (dateString) => {
    return new Date(dateString).toISOString().slice(0, 19) + " UTC";
  };

  const headers = [
    {
      field: "created_at",
      headerName: "Date",
      flex: 1.15,
      headerAlign: "center",
      align: "center",
      valueFormatter: ({ value }) => formatDateString(value),
    },
    {
      field: "operation_type",
      headerName: "Operation Type",
      flex: 1,
      headerAlign: "center",
      align: "center",
    },
    {
      field: "amount",
      headerName: "Amount",
      type: "number",
      flex: 0.75,
      headerAlign: "center",
      align: "center",
      valueFormatter: ({ value }) => Number.parseFloat(value).toFixed(2),
    },
    {
      field: "operation_response",
      headerName: "Response",
      flex: 1,
      headerAlign: "center",
      align: "center",
    },
    {
      field: "delete",
      headerName: "Delete",
      sortable: false,
      filterable: false,
      flex: 0.5,
      headerAlign: "center",
      align: "center",
      renderCell: (params) => (
        <IconButton color="error" onClick={() => handleDelete([params])}>
          <DeleteIcon />
        </IconButton>
      ),
    },
  ];

  return (
    <>
      <SearchForm searchCallBack={handleSearch}>
        <StyledButton
          variant="contained"
          color="secondary"
          startIcon={<DeleteIcon />}
          onClick={() => handleDelete(getSelectedRows(apiRef))}
        >
          Delete
        </StyledButton>
      </SearchForm>
      <Container>
        {records.length > 0 ? (
          <>
            <div>
              <XDataGrid
                title="Records"
                rows={records}
                apiRef={apiRef}
                columns={headers}
                pageSizeOptions={[10, 25, 50]}
                disableRowSelectionOnClick
                checkboxSelection
              />
            </div>
            <Typography>Total Amount: {totalAmount}</Typography>
          </>
        ) : (
          <Typography variant="h6" gutterBottom>
            No records available.
          </Typography>
        )}
      </Container>
      {alert.message && (
        <Snackbar
          identificator="records-error-snackbar"
          open={true}
          autoHideDuration={5000}
          onClose={() => setAlert({ type: "error", message: "" })}
        >
          <Alert severity={alert.type}>{alert.message}</Alert>
        </Snackbar>
      )}
    </>
  );
};

export default RecordList;
