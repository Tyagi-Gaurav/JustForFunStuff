package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.jffs.e2e.tests.core.WithPlaywrightWrapperAssertions;
import com.jffs.e2e.tests.core.WithSyntacticSugar;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jffs.e2e.tests.TestWordBuilder.aWord;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class AdminVocabularyEndToEndTests extends AbstractEndToEndTests implements WithSyntacticSugar, WithPlaywrightWrapperAssertions {
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
        final var vocabularyMenuItem = page.getByText("Vocabulary");
        assertThat(vocabularyMenuItem).isVisible();

        final var addWord = page.getByText("Add Word");
        final var listWord = page.getByText("List Words");

        assertThat(addWord).not().isVisible();
        assertThat(listWord).not().isVisible();

        vocabularyMenuItem.click();

        assertThat(addWord).isVisible();
        assertThat(listWord).isVisible();
    }

    @Test
    void listItemOnVocabShouldNotDisplayRecordsWhenNoDataReturnedByApi() {
        final var vocabularyMenuItem = page.getByText("Vocabulary");
        assertThat(vocabularyMenuItem).isVisible();

        final var listWord = page.getByText("List Words");
        vocabularyMenuItem.click();

        listWord.click();

        thenEventually(aLabel(withText("Page 0 of 3")), isVisible());
    }

    @Test
    void listItemOnVocabShouldNotDisplayRecordsWhenDataReturnedByApi() {
        for (int i = 0; i < 25; i++) {
            givenExists(aWord("Word" + i)
                    .withDefinition("Definition" + i)
                    .withSynonyms(List.of("Synonym1" + i, "Synonym2" + i))
                    .withExamples(List.of("Example1" + i, "Example2" + i)));
        }

        final var vocabularyMenuItem = page.getByText("Vocabulary");
        assertThat(vocabularyMenuItem).isVisible();

        final var listWord = page.getByText("List Words");
        vocabularyMenuItem.click();

        listWord.click();

        thenEventually(aLabel(withText("Page 1 of 3")), isVisible());
    }

    private <T> void thenEventually(Supplier<Locator> locatorSupplier, Function<Locator, T> evaluator) {
        evaluator.apply(locatorSupplier.get());
    }

    private Supplier<Locator> aLabel(String text) {
        return () -> page.getByText(text);
    }

    @AfterEach
    void tearDown() {
        if (page != null) {
            page.close();
        }
    }
}
