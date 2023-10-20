import { Backdrop, CircularProgress } from "@mui/material";

function Loading({ children, isLoading, onlyComponent }) {
  const cover = onlyComponent ? (
    <CircularProgress />
  ) : (
    <>
      <Backdrop
        sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={isLoading}
      >
        <CircularProgress color="inherit" />
      </Backdrop>
      {children}
    </>
  );

  return isLoading ? cover : <>{children}</>;
}

export default Loading;
