package com.jffs.e2e.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;


import java.time.Duration;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static java.time.Duration.*;
import static java.time.temporal.ChronoUnit.SECONDS;

class VocabularyTest {
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
    @Disabled
    void canAccessWords() {
        assertThat(page.getByText("Test your Vocabulary")).isVisible();
        assertThat(page.getByText("Can you think of the meaning before the timer runs out?")).isVisible();

        final var beginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Begin"));
        assertThat(beginButton).isVisible();

        beginButton.click();

        waitFor(of(5, SECONDS));

        final var word1 = page.getByText("Grumble");
        final var word2 = page.getByText("Staunch");
        assertThat(word1.or(word2)).isVisible();
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
