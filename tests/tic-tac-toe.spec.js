import { test, expect } from "@playwright/test";

test.describe("Main Page", () => {
  test.describe("Layout", () => {
    test("has status that shows who is the next player", async ({ page }) => {
      await gotoMainPage(page);
      const status = page.getByText("Next Player to Play: X");
      await expect(status).toBeVisible();
    });

    test("has a restart button", async ({ page }) => {
      await gotoMainPage(page);
      const restartButton = page.getByRole("button", { name: "Restart" });
      await expect(restartButton).toBeVisible();
    });

    test("has a grid of 9 squares", async ({ page }) => {
      await gotoMainPage(page);

      for (let index = 1; index <= 9; index++) {
        const square = page.locator("#square-" + index);
        await expect(square).toBeVisible();
      }
    });

    test("clicking on box displays X or O", async ({ page }) => {
      await gotoMainPage(page);
      const square1 = page.locator("#square-1");
      await square1.click();
      await expect(square1).toHaveText("X");

      const square2 = page.locator("#square-2");
      await square2.click();
      await expect(square2).toHaveText("O");
    });

    test("clicking on all boxes produces X and O alternately", async ({
      page,
    }) => {
      await gotoMainPage(page);
      let nextIsX = true;
      const moves = [1, 2, 3, 4, 5, 6, 8, 7, 9];
      for (let move of moves) {
        let nextString = nextIsX ? "X" : "O";
        const square = page.locator("#square-" + move);
        await expect(square).toBeVisible();
        await square.click();
        await expect(square).toHaveText(nextString);
        nextIsX = !nextIsX;
      }
    });

    const winning_moves = [
      { moves: [1, 2, 3, 4, 5, 6, 7], winner: "X"},
      { moves: [5, 1, 8, 3, 4, 2], winner: "O" },
    ];
    for (let winning_move of winning_moves) {
      test(`Produces ${winning_move.winner} as winner based on ${winning_move.moves}`, async ({ page }) => {
        await gotoMainPage(page);
        let nextIsX = true;
        for (let move of winning_move.moves) {
          let nextString = nextIsX ? "X" : "O";
          const square = page.locator("#square-" + move);
          await expect(square).toBeVisible();
          await square.click();
          await expect(square).toHaveText(nextString);
          nextIsX = !nextIsX;
        }
        await expect(page.getByText("Winner " + winning_move.winner)).toBeVisible();
      });
    }
  });
});
async function gotoMainPage(page) {
  await page.goto("http://localhost:3000/games/tictactoe");
}
