import { test, expect } from "@playwright/test";

test.describe("Vocabulary Testing Page", () => {
  test.describe("Layout", () => {
    test("has title", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const title = page.getByText("Test your Vocabulary");
      await expect(title).toBeVisible();
    });

    test("has label to select meaning for the word", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const title = page.getByText("Select one of the following options");
      await expect(title).toBeVisible();
    });

    test("has button labelled begin", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      await expect(page.getByRole("button", { name: "Next" })).toBeVisible();
    });

    test("has text box that shows word", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      await expect(page.getByText("Word")).toBeVisible();
    });

    test("has 4 options to select meaninigs", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      await expect(page.getByText("Meaning 1")).toBeVisible();
      await expect(page.getByText("Meaning 2")).toBeVisible();
      await expect(page.getByText("Meaning 3")).toBeVisible();
      await expect(page.getByText("Meaning 4")).toBeVisible();
    });
  });
});
