import { test, expect } from "@playwright/test";

test.describe("Home Page", () => {
  test.describe("Layout", () => {
    test("shows tic-tac-toe game card heading", async ({ page }) => {
      await page.goto("http://localhost:3000/games");
      const tictactoeHeading = page.getByTestId("tictactoe-heading");
      await expect(tictactoeHeading).toBeVisible();
    });

    test("shows tic-tac-toe game card description", async ({ page }) => {
      const description =
        "Tic-tac-toe, noughts and crosses, or Xs and Os is a paper-and-pencil " +
        "game for two players who take turns marking the spaces in a " +
        "three-by-three grid with either X or O.";
      await page.goto("http://localhost:3000/games");
      const tictactoeDescription = page.getByText(description);
      await expect(tictactoeDescription).toBeVisible();
    });

    test("shows vocabulary test description", async ({ page }) => {
      const description =
        "Let's have some fun with a vocabulary game where we learn new words—who's ready to play and become a word wizard?";
      await page.goto("http://localhost:3000/games");
      const vocabularyDescription = page.getByText(description);
      await expect(vocabularyDescription).toBeVisible();
    });
  });

  test.describe("Interaction", () => {
    test("Navigates to tic-tac-toe game page", async ({ page }) => {
      await page.goto("http://localhost:3000/games");
      const image_tag = page.getByAltText("noughts_crosses");
      await image_tag.click();
      const status = page.getByText("Next Player to Play: X");
      await expect(status).toBeVisible();
    });

    test("Navigates to vocabulary testing game page", async ({ page }) => {
      await page.goto("http://localhost:3000/games");
      const image_tag = page.getByAltText("vocab_test");
      await image_tag.click();
      const status = page.getByText("Test your Vocabulary");
      await expect(status).toBeVisible();
    });
  });
});
