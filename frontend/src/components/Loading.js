import { CircularProgress } from "@mui/material";

function Loading({ children, isLoading }) {
  return isLoading ? <CircularProgress /> : <>{children}</>;
}

export default Loading;
