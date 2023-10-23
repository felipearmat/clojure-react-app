import { DataGrid } from "@mui/x-data-grid";
import { styled } from "@mui/material/styles";
import { Typography } from "@mui/material";
import PropTypes from "prop-types";

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

const XDataGrid = (args) => {
  const pageSizeOptions = args.pageSizeOptions;
  const rows = args.rows.map((row, index) => ({ id: index, ...row }));
  const columns = args.columns.map((col) => ({
    flex: 1,
    ...col,
  }));

  return (
    <div style={{ width: "100%", minWidth: args.minWidth }}>
      {args.title && (
        <Typography variant="h4" gutterBottom>
          {args.title}
        </Typography>
      )}
      <StyledDataGrid
        rows={rows}
        columns={columns}
        pageSizeOptions={pageSizeOptions}
        initialState={{
          pagination: {
            paginationModel: {
              pageSize: (pageSizeOptions && pageSizeOptions[0]) || rows.length,
            },
          },
        }}
        {...args}
      />
    </div>
  );
};

XDataGrid.propTypes = {
  title: PropTypes.string,
  rows: PropTypes.array.isRequired,
  columns: PropTypes.array,
  pagination: PropTypes.bool,
  minWidth: PropTypes.number,
};

export default XDataGrid;
