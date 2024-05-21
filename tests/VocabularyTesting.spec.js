import { test, expect } from "@playwright/test";

test.describe("Vocabulary Testing Page", () => {
  test.describe("Layout", () => {
    test("has title", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const title = page.getByText("Test your Vocabulary");
      await expect(title).toBeVisible();
    });

    test("has button labelled begin", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      await expect(page.getByRole("button", { name: "Begin" })).toBeVisible();
    });

    test("has text box that indicates user to think of the meaning", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      await expect(page.getByText("Can you think of an answer before the timer runs out?")).toBeVisible();
    });
  });

  test.describe("Interaction", () => {
      
  });
});
