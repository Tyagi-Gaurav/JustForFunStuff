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

    const fields = [
      "meaning-text",
      "synonym-text",
      "example-text",
      "word-text",
    ];
    for (const field of fields) {
      test(`when page is loaded ${field} text are not shown`, async ({
        page,
      }) => {
        await page.goto("http://localhost:3000/games/vocabtesting");
        const fieldText = page.getByTestId(field);
        await expect(fieldText).not.toBeVisible();
      });
    }
  });

  test.describe("Interaction", () => {
    test.beforeEach(async ({ page }) => {
      showLogsOnConsoleFor(page);

      await page.route("*/**/api/words", async (route, request) => {
        expect(request.method()).toBe("GET");

        await route.fulfill({
          status: 200,
          body: JSON.stringify({
            words: [
              {
                word: "Some Word",
                meaning: ["A meaning"],
                synonyms: [
                  "Synonym1",
                  "Synonym2",
                  "Synonym3",
                  "Synonym4",
                  "Synonym5",
                ],
              },
            ],
          }),
        });
      });
    });

    test("when word is received there is text that shows Can you think of an answer before the timer runs out?", async ({
      page,
    }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const button = page.getByRole("button", { name: "Begin" });
      await expect(button).toBeVisible();
      await button.click();
      await expect(
        page.getByText("Can you think of an answer before the timer runs out?")
      ).toBeVisible();
    });

    test("when word is received it should be displayed on screen", async ({
      page,
    }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const button = page.getByRole("button", { name: "Begin" });
      await expect(button).toBeVisible();
      await button.click();

      const wordText = page.getByTestId("word-text");
      await expect(wordText).toBeVisible();
      await expect(wordText).toContainText("Some Word");
    });

    test("when word is received its synonyms should be displayed on screen", async ({
      page,
    }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const button = page.getByRole("button", { name: "Begin" });
      await expect(button).toBeVisible();
      await button.click();

      const synonyms = page.getByText("Synonyms");
      await expect(synonyms).toBeVisible();
      const fieldText = page.getByTestId("synonym-text");
      await expect(fieldText).toBeVisible();
      await expect(fieldText).toContainText("Synonym1");
      await expect(fieldText).toContainText("Synonym2");
      await expect(fieldText).toContainText("Synonym3");
      await expect(fieldText).toContainText("Synonym4");
      await expect(fieldText).toContainText("Synonym5");
    });

    test("when word is received its meaning should be displayed on screen", async ({
      page,
    }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const button = page.getByRole("button", { name: "Begin" });
      await expect(button).toBeVisible();
      await button.click();

      const synonyms = page.getByText("Meaning", {exact: true});
      await expect(synonyms).toBeVisible();
      const fieldText = page.getByTestId("meaning-text");
      await expect(fieldText).toBeVisible();
      await expect(fieldText).toHaveText("A meaning");
    });
  });
});

function showLogsOnConsoleFor(page) {
  page.on("console", (msg) => console.log(msg.text()));
}
