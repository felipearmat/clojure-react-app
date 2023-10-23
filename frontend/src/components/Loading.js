import { Backdrop, CircularProgress } from "@mui/material";

const Loading = ({ children, isLoading, onlyComponent }) => {
  const cover = onlyComponent ? (
    <CircularProgress identificator="loading-progress" />
  ) : (
    <>
      <Backdrop
        sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={isLoading}
      >
        <CircularProgress identificator="loading-progress" color="inherit" />
      </Backdrop>
      {children}
    </>
  );

  return isLoading ? cover : <>{children}</>;
};

export default Loading;
