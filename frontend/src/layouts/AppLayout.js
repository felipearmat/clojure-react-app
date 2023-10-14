import React from "react";
import { Container, Grid } from "@mui/material";
import { css } from "@emotion/react";

const classes = {
  appHeader: css`
    padding: "0",
    paddingLeft: "2rem",
    paddingRight: "2rem",
  `,
  appFooter: css`
    padding: "0.875rem",
    paddingLeft: "2rem",
    paddingRight: "2rem",
    "&.spaced": {
      paddingTop: "1.75rem",
      paddingBottom: "1.75rem",
    },
  `,
  contentContainer: css`
    padding: "0";
  `,
};

const AppLayout = ({ sidebar, header, content, footer }) => {
  return (
    <div>
      {sidebar && <div id="app_sidebar">{sidebar}</div>}
      {header && (
        <Container id="app_header" className={classes.appHeader} maxWidth="lg">
          {header}
        </Container>
      )}
      <Container id="app_content" className={classes.contentContainer}>
        <Grid container>
          <Grid item xs={12} sm={2} md={3} />
          <Grid item xs={12} sm={8} md={6}>
            {content}
          </Grid>
          <Grid item xs={12} sm={2} md={3} />
        </Grid>
      </Container>
      {footer && (
        <Container
          id="app_footer"
          className={`${classes.appFooter} ${"spaced"}`}
          maxWidth="lg"
        >
          {footer}
        </Container>
      )}
    </div>
  );
};

export default AppLayout;
