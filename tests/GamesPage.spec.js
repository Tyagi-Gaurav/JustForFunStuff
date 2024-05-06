import { test, expect } from "@playwright/test";

test.describe("Home Page", () => {
  test.describe("Layout", () => {
    test("shows tic-tac-toe game card heading", async ({ page }) => {
      await page.goto("http://localhost:3000/games");
      const tictactoeHeading = page.getByRole("heading", {
        name: "Tic-Tac-Toe",
      });
      await expect(tictactoeHeading).toBeVisible();
    });

    test("shows tic-tac-toe game card description", async ({ page }) => {
      const description =
        "Tic-tac-toe, noughts and crosses, or Xs and Os is a paper-and-pencil " +
        "game for two players who take turns marking the spaces in a " +
        "three-by-three grid with X or O.";
      await page.goto("http://localhost:3000/games");
      const tictactoeDescription = page.getByText(description);
      await expect(tictactoeDescription).toBeVisible();
    });
  });

  test.describe("Interaction", () => {
    test("Navigates to tic-tac-toe game page", async ({ page }) => {
      await page.goto("http://localhost:3000/games");
      const image_tag = page.getByAltText("noughs_crosses");
      await image_tag.click();
      const status = page.getByText("Next Player to Play: X");
      await expect(status).toBeVisible();
    });
  });
});
