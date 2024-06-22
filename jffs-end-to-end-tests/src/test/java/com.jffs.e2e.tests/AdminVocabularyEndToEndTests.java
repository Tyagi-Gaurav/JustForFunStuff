package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


class AdminVocabularyEndToEndTests extends AbstractEndToEndTests {
    private Page page;

    @BeforeEach
    void setUp() {
        page = browser.newPage();
        page.navigate("http://localhost:3001/"); //Route via /admin on nginx
        page.onConsoleMessage(x -> System.out.println(x.text()));
    }

    @Test
    void title() {
        assertThat(page).hasTitle("Just for Fun Admin");
    }

    @Test
    void hasDropDownForVocabulary() {
        Locator vocabularyMenuItem = page.getByText("Vocabulary");
        assertThat(vocabularyMenuItem).isVisible();
        Locator addWord = page.getByText("Add Word");
        Locator listWord = page.getByText("List Words");

        assertThat(addWord).not().isVisible();
        assertThat(listWord).not().isVisible();

        vocabularyMenuItem.click();

        assertThat(addWord).isVisible();
        assertThat(listWord).isVisible();
    }

    @AfterEach
    void tearDown() {
        if (page != null) {
            page.close();
        }
    }
}
