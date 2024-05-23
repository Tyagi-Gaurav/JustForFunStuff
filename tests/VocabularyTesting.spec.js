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
                examples : [
                  "Example 1",
                  "Example 2"
                ]
              },
            ],
          }),
        });
      });
    });

    test("when word is received the timer should be displayed", async ({
      page,
    }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");

      const text = page.getByText("Can you think of the meaning before the timer runs out?")
      await expect(text).toBeVisible();

      const button = page.getByRole("button", { name: "Begin" });
      await expect(button).toBeVisible();
      await button.click();

      await expect(page.getByTestId("countdown")).toBeVisible();
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

      const synonyms = page.getByText("Meaning", { exact: true });
      await expect(synonyms).toBeVisible();
      const fieldText = page.getByTestId("meaning-text");
      await expect(fieldText).toBeVisible();
      await expect(fieldText).toHaveText("A meaning");
    });

    test("when word is received its examples should be displayed on screen", async ({
      page,
    }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const button = page.getByRole("button", { name: "Begin" });
      await expect(button).toBeVisible();
      await button.click();

      const synonyms = page.getByText("Examples", { exact: true });
      await expect(synonyms).toBeVisible();
      const fieldText = page.getByTestId("example-text");
      await expect(fieldText).toBeVisible();
      await expect(fieldText).toContainText("Example 1");
      await expect(fieldText).toContainText("Example 2");
    });

    test("when word is received begin button should be changed to Next", async ({
      page,
    }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      const beginButton = page.getByRole("button", { name: "Begin" });
      await expect(beginButton).toBeVisible();
      await beginButton.click();

      const synonyms = page.getByText("Meaning", { exact: true });
      await expect(synonyms).toBeVisible();
      await expect(beginButton).not.toBeVisible();

      const nextButton = page.getByRole("button", { name: "Next" });
      await expect(nextButton).toBeVisible();
    });

    test("when word has no examples, then it should not show examples", async ({
      page,
    }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      await page.route("*/**/api/words", async (route, request) => {
        expect(request.method()).toBe("GET");

        await route.fulfill({
          status: 200,
          body: JSON.stringify({
            words: [
              {
                word: "Some Word",
                meaning: ["A meaning"],
                synonyms: ["Synonym1", "Synonym2"],
              },
            ],
          }),
        });
      });

      const beginButton = page.getByRole("button", { name: "Begin" });
      await expect(beginButton).toBeVisible();
      await beginButton.click();

      const example = page.getByText("Example", { exact: true });
      await expect(example).not.toBeVisible();

      const fieldText = page.getByTestId("example-text");
      await expect(fieldText).not.toBeVisible();
    });

    test("when word has no synonyms, then it should not show synonyms", async ({
      page,
    }) => {
      await page.goto("http://localhost:3000/games/vocabtesting");
      await page.route("*/**/api/words", async (route, request) => {
        expect(request.method()).toBe("GET");

        await route.fulfill({
          status: 200,
          body: JSON.stringify({
            words: [
              {
                word: "Some Word",
                meaning: ["A meaning"]
              },
            ],
          }),
        });
      });

      const beginButton = page.getByRole("button", { name: "Begin" });
      await expect(beginButton).toBeVisible();
      await beginButton.click();

      const synonyms = page.getByText("Synonyms", { exact: true });
      await expect(synonyms).not.toBeVisible();

      const fieldText = page.getByTestId("synonym-text");
      await expect(fieldText).not.toBeVisible();
    });
  });
});

function showLogsOnConsoleFor(page) {
  page.on("console", (msg) => console.log(msg.text()));
}
