import PropTypes from "prop-types";
import { DataGrid } from "@mui/x-data-grid";
import { Typography } from "@mui/material";
import { styled } from "@mui/material/styles";

const StyledDataGrid = styled(DataGrid)(({ theme }) => ({
  ".MuiDataGrid-columnHeader": {
    backgroundColor: theme?.palette?.common?.black,
    color: theme?.palette?.common?.white,
    "& .MuiSvgIcon-root": {
      color: "rgba(255, 255, 255, 0.54)",
    },
  },
  ".MuiDataGrid-row": {
    "&:nth-of-type(odd)": {
      backgroundColor: theme?.palette?.action?.hover,
    },
  },
}));

function XDataGrid({ title, rows, columns, pageSizeOptions, minWidth = 400 }) {
  const data = rows.map((row, index) => ({ id: index, ...row }));
  const columnsDefs = columns.map((col) => ({
    field: col.id,
    headerName: col.label,
    flex: 1,
    align: col.align,
  }));

  return (
    <div style={{ width: "100%", minWidth: minWidth }}>
      {title && (
        <Typography variant="h4" gutterBottom>
          {title}
        </Typography>
      )}
      <StyledDataGrid
        rows={data}
        columns={columnsDefs}
        pageSizeOptions={pageSizeOptions}
        initialState={{
          pagination: {
            paginationModel: {
              pageSize: (pageSizeOptions && pageSizeOptions[0]) || rows.length,
            },
          },
        }}
        checkboxSelection
        disableSelectionOnClick
      />
    </div>
  );
}

XDataGrid.propTypes = {
  title: PropTypes.string,
  rows: PropTypes.array.isRequired,
  columns: PropTypes.array,
  pagination: PropTypes.bool,
  minWidth: PropTypes.number,
};

export default XDataGrid;
