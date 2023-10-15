import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Routes from "./routes";

const container = document.getElementById("root");

const root = ReactDOM.createRoot(container);

const router = createBrowserRouter(Routes);

root.render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
