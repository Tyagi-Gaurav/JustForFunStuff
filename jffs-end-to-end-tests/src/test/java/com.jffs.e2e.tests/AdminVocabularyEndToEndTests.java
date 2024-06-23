package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.jffs.e2e.tests.core.WithPlaywrightWrapperAssertions;
import com.jffs.e2e.tests.core.WithSyntacticSugar;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jffs.e2e.tests.TestWordBuilder.aWord;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class AdminVocabularyEndToEndTests extends AbstractEndToEndTests implements WithSyntacticSugar {
    @Nested
    class LandingPage {
        @BeforeEach
        void setup() {
            page.navigate("http://localhost:3001/");
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
    }

    @Nested
    class ListItem {
        @BeforeEach
        void setup() {
            page.navigate("http://localhost:3001/");
        }

        @Test
        void listItemOnVocabShouldNotDisplayRecordsWhenNoDataReturnedByApi() {
            final var vocabularyMenuItem = page.getByText("Vocabulary");
            assertThat(vocabularyMenuItem).isVisible();

            final var listWord = page.getByText("List Words");
            vocabularyMenuItem.click();

            listWord.click();

            thenEventually(aLabel(withText("Page of")), isVisible());
        }

        @Test
        void listItemOnVocabShouldDisplayRecordsWhenDataReturnedByApi() {
            for (int i = 1; i <= 25; i++) {
                givenExists(aWord("Word" + i)
                        .withDefinition("Definition" + i)
                        .withSynonyms(List.of("Synonym1" + i, "Synonym2" + i))
                        .withExamples(List.of("Example1" + i, "Example2" + i)));
            }

//            final var vocabularyMenuItem = page.getByText("Vocabulary");
//            assertThat(vocabularyMenuItem).isVisible();
//            vocabularyMenuItem.click();
//
//            final var listWord = page.getByText("List Words");
//            assertThat(listWord).isVisible();
//            listWord.click();
//
//            thenEventually(aLabel(withText("Page 1 of 3")), isVisible());
        }
    }

    private Supplier<Locator> aLabel(String text) {
        return () -> page.getByText(text);
    }
}
