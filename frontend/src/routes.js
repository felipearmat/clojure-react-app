import App from "./App";
import Records from "./views/Records";
import { Navigate } from "react-router-dom";
import Operations from "./views/Operations";
// import UserProfile from "./views/UserProfile";
// import IncreaseCredit from "./views/IncreaseCredit";
// import UserProfile from "./views/UserProfile";

const routes = [
  {
    path: "/",
    element: <App />,
    children: [
      {
        path: "/",
        element: <Records />,
      },
      {
        path: "/operations",
        element: <Operations />,
      },
      // {
      //   path: "/increase-credit",
      //   element: <IncreaseCredit />,
      // },
      {
        path: "*",
        element: <Navigate to="/" />,
      },
    ],
  },
];

export default routes;
