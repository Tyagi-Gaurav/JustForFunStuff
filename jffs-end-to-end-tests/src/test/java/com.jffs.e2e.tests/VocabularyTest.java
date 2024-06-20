package com.jffs.e2e.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;


import java.time.Duration;
import java.util.List;

import static com.jffs.e2e.tests.TestWordBuilder.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static java.time.Duration.*;
import static java.time.temporal.ChronoUnit.SECONDS;

class VocabularyTest extends AbstractEndToEndTests {
    static Playwright playwright;
    static Browser browser;
    private Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @BeforeEach
    void setUp() {
        page = browser.newPage();
        page.navigate("http://localhost/games/vocabtesting");
    }

    @Test
    void canAccessWords() {
        givenASetOfWordsHaveBeenCreated(aWord("some-word")
                .withDefinition("A definition")
                .withSynonyms(List.of("Synonym1", "Synonym2"))
                .withExamples(List.of("Example1", "Example2"))
                .build());

        assertThat(page.getByText("Test your Vocabulary")).isVisible();
        assertThat(page.getByText("Can you think of the meaning before the timer runs out?")).isVisible();

        final var beginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Begin"));
        assertThat(beginButton).isVisible();

        beginButton.click();

        waitFor(of(5, SECONDS));

        final var word = page.getByTestId("word-text");
        assertThat(word).isVisible();
        assertThat(word.getByText("some-word")).isVisible();
    }

    private void waitFor(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void afterAll() {
        browser.close();
    }
}
