import { act } from "react-dom/test-utils";
import { cleanup, fireEvent, render } from "@testing-library/react";
import App from "../src/App";
import axios from "axios";

describe("App Component", () => {
  afterEach(cleanup);

  describe("when user is not logged on startup", () => {
    beforeEach(async () => {
      axios.get.mockResolvedValue({
        data: { logged: null },
      });

      render(<App />);

      // Wait for the minimum time for a request to happen
      await act(() => new Promise((resolve) => setTimeout(resolve, 0)));
    });

    it("LoginForm should be rendered", () => {
      const loginForm = document.querySelector(
        "[identificator='app-login-form']"
      );

      expect(loginForm).toBeInTheDocument();
    });

    it("Outlet should be rendered after user logs in", async () => {
      // mock response for post request on backend
      axios.post.mockResolvedValue({});

      // mock new get request triggered by authCallBack
      axios.get.mockResolvedValue({
        data: { logged: true, balance: 100, email: "test@example.com" },
      });

      fireEvent.click(
        document.querySelector("[identificator='login-form-sign-in']")
      );

      // Wait for the minimum time for a request to happen
      await act(() => new Promise((resolve) => setTimeout(resolve, 0)));

      const outletComponent = document.querySelector(
        "[identificator='app-outlet']"
      );

      expect(outletComponent).toBeInTheDocument();
    });
  });

  describe("when user is logged on startup", () => {
    beforeEach(async () => {
      axios.get.mockResolvedValue({
        data: { logged: true, balance: 100, email: "test@example.com" },
      });

      render(<App />);

      // Wait for the minimum time for a request to happen
      await act(() => new Promise((resolve) => setTimeout(resolve, 0)));
    });

    it("Outlet should be rendered", () => {
      const outletComponent = document.querySelector(
        "[identificator='app-outlet']"
      );

      expect(outletComponent).toBeInTheDocument();
    });
  });
});
