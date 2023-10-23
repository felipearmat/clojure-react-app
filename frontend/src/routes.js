import App from "./App";
import Records from "./views/Records";
import { Navigate } from "react-router-dom";
import CalculatorView from "./views/CalculatorView";

const routes = [
  {
    path: "/",
    element: <App />,
    children: [
      {
        path: "/",
        label: "Home",
        element: <CalculatorView />,
      },
      {
        path: "/records",
        label: "Records",
        element: <Records />,
      },
      {
        path: "*",
        element: <Navigate to="/" />,
      },
    ],
  },
];

const navMaker = () => {
  const result = [];
  routes[0].children.forEach((route) => {
    if (route.path && route.path !== "*" && route.label) {
      result.push({ path: route.path, label: route.label });
    }
  });

  return result;
};

export default routes;

export { navMaker };
