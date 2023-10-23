import { act, cleanup, fireEvent, render } from "@testing-library/react";
import axios from "axios";
import Calculator from "../../src/components/Calculator";

var targetElement = null;

describe("Calculator component", () => {
  beforeEach(() => {
    render(<Calculator />);
  });

  afterEach(cleanup);

  describe("rendering necessary elements", () => {
    it("- calculator history title", () => {
      const historyTitle = document.querySelector(
        "[identificator='calculator-history-title']"
      );
      expect(historyTitle).toBeInTheDocument();
    });

    it("- calculator readonly input", () => {
      targetElement = document.querySelector("input[readonly]");
      expect(targetElement).toBeInTheDocument();
      expect(targetElement).toHaveValue("");
    });

    it("- calculator buttons", () => {
      const buttons = document.querySelectorAll(
        "[identificator^='calculator-button-']"
      );
      expect(buttons).toHaveLength(22);
    });
  });

  describe("input events behaviour", () => {
    beforeEach(() => {
      targetElement = document.querySelector("input[readonly]");

      fireEvent.click(
        document.querySelector("[identificator='calculator-button-1']")
      );
    });

    it("should register consecutive button", () => {
      expect(targetElement).toHaveValue("1");

      fireEvent.click(
        document.querySelector("[identificator='calculator-button-2']")
      );
      expect(targetElement).toHaveValue("12");

      fireEvent.click(
        document.querySelector("[identificator='calculator-button-3']")
      );
      expect(targetElement).toHaveValue("123");

      fireEvent.click(
        document.querySelector("[identificator='calculator-button-+']")
      );
      expect(targetElement).toHaveValue("123+");
    });

    it("then clears the input when pressing C", () => {
      fireEvent.click(
        document.querySelector("[identificator='calculator-button-C']")
      );

      expect(targetElement).toHaveValue("");
    });
  });

  describe("calculation behaviour", () => {
    beforeEach(async () => {
      axios.post.mockResolvedValue({
        data: { result: 6, balance: 100 },
      });

      fireEvent.click(
        document.querySelector("[identificator='calculator-button-4']")
      );
      fireEvent.click(
        document.querySelector("[identificator='calculator-button-+']")
      );
      fireEvent.click(
        document.querySelector("[identificator='calculator-button-2']")
      );
      fireEvent.click(
        document.querySelector("[identificator='calculator-button-=']")
      );

      await act(() => new Promise((resolve) => setTimeout(resolve, 0)));
    });

    it("should clear the readonly input after pressing '='", () => {
      targetElement = document.querySelector("input[readonly]");
      expect(targetElement).toHaveValue("");
    });

    it("should add the expression and result into the history", () => {
      const historyItem = document.querySelector(
        "[identificator^='calculator-history-item-']"
      );
      expect(historyItem).toBeInTheDocument();
      expect(historyItem.textContent).toEqual("4+2 = 6");
    });
  });

  describe("error handling behaviour", () => {
    beforeEach(async () => {
      axios.post.mockRejectedValue(new Error("Invalid expression"));

      targetElement = document.querySelector("input[readonly]");

      fireEvent.click(
        document.querySelector("[identificator='calculator-button-1']")
      );
      fireEvent.click(
        document.querySelector("[identificator='calculator-button-+']")
      );
      fireEvent.click(
        document.querySelector("[identificator='calculator-button-=']")
      );

      await act(() => new Promise((resolve) => setTimeout(resolve, 0)));
    });

    it("should not clear readonly input on error", () => {
      expect(targetElement).toHaveValue("1+");
    });

    it("should show a snackbar with error message", () => {
      const errorSnackbar = document.querySelector(
        "[identificator='calculator-error-snackbar']"
      );
      expect(errorSnackbar).toBeInTheDocument();
      expect(errorSnackbar.textContent).toEqual("Invalid expression");
    });
  });
});
