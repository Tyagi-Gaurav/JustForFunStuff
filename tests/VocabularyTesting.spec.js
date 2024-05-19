import { test, expect } from "@playwright/test";

test.describe("Vocabulary Testing Page", () => {
  test.describe("Layout", () => {
    test("has title", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const title = page.getByText("Test your Vocabulary");
      await expect(title).toBeVisible();
    });

    test("has label to select word count", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const title = page.getByText("Word Count");
      await expect(title).toBeVisible();
    });
  });
});
