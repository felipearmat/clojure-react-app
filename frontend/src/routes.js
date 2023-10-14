import React from "react";
import App from "./App";
import Records from "./views/Records";
import { Navigate } from "react-router-dom";
// import Operations from "./views/Operations";
// import IncreaseCredit from "./views/IncreaseCredit";
// import UserProfile from "./views/UserProfile";

const routes = [
  {
    path: "/",
    element: <App />,
    children: [
      {
        path: "/records",
        element: <Records />,
      },
      // {
      //   path: "/operations",
      //   element: <Operations />,
      // },
      // {
      //   path: "/increase-credit",
      //   element: <IncreaseCredit />,
      // },
      // {
      //   path: "/profile",
      //   element: <UserProfile />,
      // },
      {
        path: "*",
        element: <Navigate to="/records" />,
      },
    ],
  },
];

export default routes;
