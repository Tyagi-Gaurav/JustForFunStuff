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

    private static final String PREVIOUS_BUTTON = "< Previous";
    private static final String NEXT_BUTTON = "Next >";

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
            givenListItemIsClicked();
            thenEventually(aLabel(withText("Page 1 of 0")), isVisible());
        }

        @Test
        void listItemOnVocabShouldDisplayRecordsWhenDataReturnedByApi() {
            givenSomeWordsExist();

            givenListItemIsClicked();

            thenEventually(aLabel(withText("Page 1 of 3")), isVisible());
            thenEventually(aButton(withName("< Previous")), isVisible());
            thenEventually(aButton(withName("Next >")), isVisible());
        }

        @Test
        void listItemCheckNextButton() {
            givenSomeWordsExist();

            givenListItemIsClicked();

            thenEventually(aLabel(withText("Page 1 of 3")), isVisible());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isVisible());
            thenEventually(aButton(withName(NEXT_BUTTON)), isVisible());

            when(aButton(withName(NEXT_BUTTON)), isClicked());
            thenEventually(aLabel(withText("Page 2 of 3")), isVisible());

            when(aButton(withName(NEXT_BUTTON)), isClicked());
            thenEventually(aLabel(withText("Page 3 of 3")), isVisible());
            and(aButton(withName(NEXT_BUTTON)), isVisible());
            and(aButton(withName(NEXT_BUTTON)), isDisabled());
        }

        @Test
        void listItemCheckPreviousButton() {
            givenSomeWordsExist();

            givenListItemIsClicked();

            thenEventually(aLabel(withText("Page 1 of 3")), isVisible());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isVisible());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isDisabled());
            thenEventually(aButton(withName(NEXT_BUTTON)), isVisible());

            when(aButton(withName(NEXT_BUTTON)), isClicked());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isEnabled());

            when(aButton(withName(NEXT_BUTTON)), isClicked());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isEnabled());

            when(aButton(withName(PREVIOUS_BUTTON)), isClicked());
            thenEventually(aLabel(withText("Page 2 of 3")), isVisible());

            when(aButton(withName(PREVIOUS_BUTTON)), isClicked());
            thenEventually(aLabel(withText("Page 1 of 3")), isVisible());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isDisabled());
        }
    }

    private void givenListItemIsClicked() {
        given(aMenuItem(withText("Vocabulary")), isVisible());
        and(aMenuItem(withText("Vocabulary")), isClicked());

        thenEventually(aMenuItem(withName("List Words")), isVisible());
        and(aMenuItem(withName("List Words")), isClicked());
    }
}
