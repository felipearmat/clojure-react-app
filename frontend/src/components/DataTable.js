import React, { useState } from "react";
import PropTypes from "prop-types";
import { styled } from "@mui/material/styles";
import { tableCellClasses } from "@mui/material/TableCell";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  Typography,
} from "@mui/material";

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  "&:nth-of-type(odd)": {
    backgroundColor: theme.palette.action.hover,
  },
  "&:last-child td, &:last-child th": {
    border: 0,
  },
}));

function DataTableHeader({ headers }) {
  return (
    <TableHead>
      <TableRow>
        {headers.map((header) => (
          <StyledTableCell
            key={header.id}
            align={header.align}
            style={{ minWidth: header.minWidth }}
          >
            {header.label}
          </StyledTableCell>
        ))}
      </TableRow>
    </TableHead>
  );
}

function DataTableBody({ rows, headers }) {
  return (
    <TableBody>
      {rows.map((row, idx) => (
        <StyledTableRow role="checkbox" tabIndex={-1} key={idx}>
          {headers.map((header) => (
            <StyledTableCell key={header.id} align={header.align}>
              {header.format ? header.format(row[header.id]) : row[header.id]}
            </StyledTableCell>
          ))}
        </StyledTableRow>
      ))}
    </TableBody>
  );
}

DataTable.propTypes = {
  title: PropTypes.string,
  rows: PropTypes.array.isRequired,
  columns: PropTypes.array,
  pagination: PropTypes.bool,
  minWidth: PropTypes.number,
};

function DataTable({ title, rows, columns, pagination, minWidth = 400 }) {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const dataLength = columns
    ? columns.length
    : rows.length > 0
    ? rows[0].length
    : 0;

  const headerDefaults = {
    align: "center",
    minWidth: minWidth / dataLength,
  };

  const getHeaders = () => {
    if (columns) {
      return columns.map((item) => ({
        ...headerDefaults,
        ...item,
      }));
    } else if (rows.length > 0) {
      const sampleRow = rows[0];
      const sampleData = Object.keys(sampleRow).sort();
      return sampleData.map((key) => ({
        ...headerDefaults,
        id: key,
        label: key,
      }));
    }
    return [];
  };

  const headers = getHeaders();

  const handleChangePage = (_event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  return (
    <TableContainer sx={{ width: "100%", overflow: "hidden" }}>
      {title && (
        <Typography variant="h4" gutterBottom>
          {title}
        </Typography>
      )}
      <Table sx={{ minWidth }} aria-label="customized table">
        <DataTableHeader headers={headers} />
        <DataTableBody rows={rows} headers={headers} />
      </Table>
      {pagination && (
        <TablePagination
          rowsPerPageOptions={[10, 25, 50, 100]}
          component="div"
          count={rows.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      )}
    </TableContainer>
  );
}

export default DataTable;
