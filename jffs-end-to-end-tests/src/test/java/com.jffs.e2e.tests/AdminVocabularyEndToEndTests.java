package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


class AdminVocabularyEndToEndTests extends AbstractEndToEndTests {
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
        page.navigate("http://localhost:3001/"); //Route via /admin on nginx
        page.onConsoleMessage(x -> System.out.println(x.text()));
    }

    @Test
    void title() {
        assertThat(page.getByText("Admin")).isVisible();
    }
}
