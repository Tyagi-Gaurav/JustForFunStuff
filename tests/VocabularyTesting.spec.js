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

    const fields = ["meaning-text", "synonym-text", "example-text"];
    for (const field of fields) {
      test(`when page is loaded ${field} text are not shown`, async ({
        page,
      }) => {
        await page.goto("http://localhost:3000/games/vocabtesting");
        const meaning = page.getByTestId(field);
        await expect(meaning).not.toBeVisible();
      });
    }
  });

  test.describe("Interaction", () => {
    test("when word is received there is text that shows meaning, synonyms and example", async ({
      page,
    }) => {
      await page.route("*/**/api/words", async (route, request) => {
        expect(request.method()).toBe("GET");

        await route.fulfill({
          status: 200,
          body: JSON.stringify({
            message: JSON.stringify({
              word: "Staunch",
              meaning: ["Very loyal and committed in attitude"],
              synonyms: [
                "Stalwart",
                "Loyal",
                "Faithful",
                "Trusty",
                "Committed",
              ],
            }),
          }),
        });
      });

      await page.goto("http://localhost:3000/games/vocabtesting");
      const button = page.getByRole("button", { name: "Begin" });
      await expect(button).toBeVisible();
      await button.click();
      await expect(page.getByText("Can you think of an answer before the timer runs out?")
      ).toBeVisible();
      
      const synonyms = page.getByText("Synonyms");
      await expect(synonyms).toBeVisible();

      const example = page.getByText("Example");
      await expect(example).toBeVisible();

      const meaning = page.getByTestId("meaning-text");
      await expect(meaning).toBeVisible();
    });
  });
});
