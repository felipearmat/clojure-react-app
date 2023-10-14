import { Global, css } from "@emotion/react";
import { CssBaseline } from "@mui/material";

function GlobalCss() {
  return (
    <>
      <CssBaseline />
      <Global
        styles={css`
          body {
            margin: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Roboto",
              "Oxygen", "Ubuntu", "Cantarell", "Fira Sans", "Droid Sans",
              "Helvetica Neue", sans-serif;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
            background-color: #f7f7f7; /* Change to your desired background color */
            color: #333; /* Change to your desired text color */
          }
          code {
            font-family: source-code-pro, Menlo, Monaco, Consolas, "Courier New",
              monospace;
          }
        `}
      />
    </>
  );
}

export default GlobalCss;
