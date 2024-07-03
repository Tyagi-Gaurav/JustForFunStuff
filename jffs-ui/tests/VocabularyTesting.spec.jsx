import { BrowserRouter } from "react-router-dom";
import { describe, test, expect } from "vitest";
import VocabularyTesting from "../src/pages/VocabularyTesting";
import { render, screen, act, waitFor } from "@testing-library/react";

describe("Vocabulary Testing Page", () => {
  describe("Layout", () => {
    test("has title", async ({ page }) => {
      render(
        <BrowserRouter>
          <VocabularyTesting />
        </BrowserRouter>
      );

      const title = screen.getByText("Test your Vocabulary");
      expect(title).toBeVisible();
    });

    test("has button labelled begin", async ({ page }) => {
      render(
        <BrowserRouter>
          <VocabularyTesting />
        </BrowserRouter>
      );

      expect(screen.getByRole("button", { name: "Begin" })).toBeVisible();
    });

    test.each(["meaning-text", "synonym-text", "example-text", "word-text"])(
      "when page is loaded %s is not shown",
      (field) => {
        render(
          <BrowserRouter>
            <VocabularyTesting />
          </BrowserRouter>
        );

        const fieldText = screen.queryByTestId(field);
        expect(fieldText).toBeNull();
      }
    );
  });

  describe("Interaction", () => {
    test("Show error when no words available on begin", async () => {
      render(
        <BrowserRouter>
          <VocabularyTesting />
        </BrowserRouter>
      );

      const beginButton = screen.getByRole('button', {name: "Begin"});
      expect(beginButton).toBeVisible();
      act(() => beginButton.click());

      await waitFor(() => {
        const errorText = screen.getByText("There seems to be some problem. Please try again later");
        expect(errorText).toBeVisible();
      });
    });
  });
});
