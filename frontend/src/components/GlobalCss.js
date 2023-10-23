import { css, Global } from "@emotion/react";
import { CssBaseline } from "@mui/material";

const GlobalCss = () => {
  return (
    <>
      <CssBaseline />
      <Global
        styles={css`
          #root {
            min-width: 350px;
          }
          #root > div {
            padding: 0;
          }
          body {
            margin: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Roboto",
              "Oxygen", "Ubuntu", "Cantarell", "Fira Sans", "Droid Sans",
              "Helvetica Neue", sans-serif;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
            background-color: #f7f7f7;
            color: #333;
          }
          code {
            font-family: source-code-pro, Menlo, Monaco, Consolas, "Courier New",
              monospace;
          }
        `}
      />
    </>
  );
};

export default GlobalCss;
