import { test, expect } from "@playwright/test";

test.describe("Main Page", () => {
  test.describe("Layout", () => {
    test("has status that shows who is the next player", async ({ page }) => {
      await gotoMainPage(page);
      const status = page.getByText("Next Player: X");
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
        const square = page.locator("#square-"+index);
        await expect(square).toBeVisible();
      }
    });
  });
});
async function gotoMainPage(page) {
    await page.goto("http://localhost:3000");
}

