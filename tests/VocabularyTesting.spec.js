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

    test("has text that indicates user to think of the meaning", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      await expect(page.getByText("Can you think of an answer before the timer runs out?")).toBeVisible();
    });

    test("has text that can show user the meaning", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const label = page.getByText("Meaning")
      await expect(label).toBeVisible();

      const textArea = page.getByTestId("meaning-text");
      await expect(textArea).toBeVisible();
    });

    test("has text that can show user the synonyms", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const label = page.getByText("Synonyms")
      await expect(label).toBeVisible();

      const textArea = page.getByTestId("synonym-text");
      await expect(textArea).toBeVisible();
    });

    test("has text that can show user the Example", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const label = page.getByText("Example", { exact: true })
      await expect(label).toBeVisible();

      const textArea = page.getByTestId("example-text");
      await expect(textArea).toBeVisible();
    });

    test("has text that can show user the synonym", async ({ page }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      await expect(page.getByText("Can you think of an answer before the timer runs out?")).toBeVisible();
    });
  });

  test.describe("Interaction", () => {
      
  });
});
