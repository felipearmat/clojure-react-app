import { Navigate } from "react-router-dom";
import App from "./App";
import CalculatorView from "./views/CalculatorView";
import Records from "./views/Records";

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
