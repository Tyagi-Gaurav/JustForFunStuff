package com.jffs.e2e.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
        page.navigate("http://localhost:3000/games/vocabtesting");
    }

    @Test
    void errorMessageAppearsWhenNoWordsAreFound() {
        assertThat(page.getByText("Test your Vocabulary")).isVisible();
        assertThat(page.getByText("Can you think of the meaning before the timer runs out?")).isVisible();

        final var beginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Begin"));
        assertThat(beginButton).isVisible();
        final var errorText = page.getByText("There seems to be some problem. Please try again later");
        assertThat(errorText).not().isVisible();

        beginButton.click();

        assertThat(errorText).isVisible();
    }

    @AfterAll
    static void afterAll() {
        browser.close();
    }
}
