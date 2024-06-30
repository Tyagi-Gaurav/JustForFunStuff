package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.jffs.e2e.tests.core.WithAdminApp;
import com.jffs.e2e.tests.core.WithSyntacticSugar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.jffs.e2e.tests.TestWordBuilder.aWord;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class AdminVocabularyEndToEndTests extends AbstractEndToEndTests implements WithSyntacticSugar, WithAdminApp {
    @Nested
    class LandingPage {
        @BeforeEach
        void setup() {
            page.navigate(adminAppUrl());
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
            page.navigate(adminAppUrl());
        }

        @Test
        void listItemOnVocabShouldNotDisplayRecordsWhenNoDataReturnedByApi() {
            given(aMenuItem(withText("Vocabulary")), isVisible());
            and(aMenuItem(withText("Vocabulary")), isClicked());

            thenEventually(aMenuItem(withName("List Words")), isVisible());
            and(aMenuItem(withName("List Words")), isClicked());
            thenEventually(aLabel(withText("Page 1 of 0")), isVisible());
        }

        @Test
        void listItemOnVocabShouldDisplayRecordsWhenDataReturnedByApi() {
            givenSomeWordsExist();

            given(aMenuItem(withText("Vocabulary")), isVisible());
            and(aMenuItem(withText("Vocabulary")), isClicked());

            thenEventually(aMenuItem(withName("List Words")), isVisible());
            and(aMenuItem(withName("List Words")), isClicked());

            thenEventually(aLabel(withText("Page 1 of 3")), isVisible());
            thenEventually(aButton(withName("< Previous")), isVisible());
            thenEventually(aButton(withName("Next >")), isVisible());
        }

        @Test
        void listItemCheckNextButton() {
            givenSomeWordsExist();

            given(aMenuItem(withText("Vocabulary")), isVisible());
            and(aMenuItem(withText("Vocabulary")), isClicked());

            thenEventually(aMenuItem(withName("List Words")), isVisible());
            and(aMenuItem(withName("List Words")), isClicked());

            thenEventually(aLabel(withText("Page 1 of 3")), isVisible());
            thenEventually(aButton(withName("< Previous")), isVisible());
            thenEventually(aButton(withName("Next >")), isVisible());

            when(aButton(withName("Next >")), isClicked());
            thenEventually(aLabel(withText("Page 2 of 3")), isVisible());

            when(aButton(withName("Next >")), isClicked());
            thenEventually(aLabel(withText("Page 3 of 3")), isVisible());
            and(aButton(withName("Next >")), isVisible());
            and(aButton(withName("Next >")), isDisabled());
        }

    }

    private void givenSomeWordsExist() {
        for (int i = 1; i <= 25; i++) {
            givenExists(aWord("Word" + i)
                    .withDefinition("Definition" + i)
                    .withSynonyms(List.of("Synonym1" + i, "Synonym2" + i))
                    .withExamples(List.of("Example1" + i, "Example2" + i)));
        }
    }
}
