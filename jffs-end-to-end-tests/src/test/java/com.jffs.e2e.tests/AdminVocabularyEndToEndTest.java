package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.jffs.e2e.tests.core.WithAdminApp;
import com.jffs.e2e.tests.core.WithSyntacticSugar;
import com.jffs.e2e.tests.core.assertion.TestWordAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.jffs.e2e.tests.TestWordBuilder.aWord;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class AdminVocabularyEndToEndTest extends AbstractEndToEndTests implements WithSyntacticSugar, WithAdminApp {

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
            thenEventually(aLabel(byText("Page 1 of 0")), isVisible());
        }

        @Test
        void listItemOnVocabShouldDisplayRecordsWhenDataReturnedByApi() {
            givenSomeWordsExist();

            givenListItemIsClicked();

            thenEventually(aLabel(byText("Page 1 of 3")), isVisible());
            thenEventually(aButton(withName("< Previous")), isVisible());
            thenEventually(aButton(withName("Next >")), isVisible());
        }

        @Test
        void listItemCheckNextButton() {
            givenSomeWordsExist();

            givenListItemIsClicked();

            thenEventually(aLabel(byText("Page 1 of 3")), isVisible());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isVisible());
            thenEventually(aButton(withName(NEXT_BUTTON)), isVisible());

            when(aButton(withName(NEXT_BUTTON)), isClicked());
            thenEventually(aLabel(byText("Page 2 of 3")), isVisible());

            when(aButton(withName(NEXT_BUTTON)), isClicked());
            thenEventually(aLabel(byText("Page 3 of 3")), isVisible());
            and(aButton(withName(NEXT_BUTTON)), isVisible());
            and(aButton(withName(NEXT_BUTTON)), isDisabled());
        }

        @Test
        void listItemCheckPreviousButton() {
            givenSomeWordsExist();

            givenListItemIsClicked();

            thenEventually(aLabel(byText("Page 1 of 3")), isVisible());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isVisible());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isDisabled());
            thenEventually(aButton(withName(NEXT_BUTTON)), isVisible());

            when(aButton(withName(NEXT_BUTTON)), isClicked());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isEnabled());

            when(aButton(withName(NEXT_BUTTON)), isClicked());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isEnabled());

            when(aButton(withName(PREVIOUS_BUTTON)), isClicked());
            thenEventually(aLabel(byText("Page 2 of 3")), isVisible());

            when(aButton(withName(PREVIOUS_BUTTON)), isClicked());
            thenEventually(aLabel(byText("Page 1 of 3")), isVisible());
            thenEventually(aButton(withName(PREVIOUS_BUTTON)), isDisabled());
        }
    }

    @Nested
    class DeleteItem {
        @Test
        void deleteWord() throws Exception {
            page.navigate(adminAppUrl());
            givenSomeWordsExist();

            givenExists(aWord("WordToBeDeleted")
                    .withDefinition("A Definition that should be deleted")
                    .withSynonyms(List.of("Synonym1", "Synonym2"))
                    .withExamples(List.of("Example1", "Example2")));

            givenListItemIsClicked();
            thenEventually(aLabel(byText("Page 1 of 3")), isVisible());
            given(textBoxLabelled("Word"), isFilledWith("WordToBeDeleted"));
            given(aButton(withName("GO")), isClicked());

            thenEventually(aCell(withText("WordToBeDeleted")), isVisible());
            given(aCell(withText("WordToBeDeleted")), isClicked());
            and(aButton(withName("DELETE")), isVisible());
            when(aButton(withName("DELETE")), isClicked());

            Assertions.assertThat(doesWordExist("WordToBeDeleted")).isFalse();
        }
    }

    @Nested
    class AddItem {
        @Test
        void addWord() {
            page.navigate(adminAppUrl());
            givenSomeWordsExist();
            givenAddItemIsClicked();

            given(textBoxLabelled("Word"), isFilledWith("WordToBeAdded"));
            given(textBoxLabelled("Meaning"), isFilledWith("A Definition to be added"));
            given(textBoxLabelled("Synonyms"), isFilledWith("Synonym1,Synonym2"));
            given(textBoxLabelled("Examples"), isFilledWith("Example1,Example2"));
            given(aButton(withName("SUBMIT")), isClicked());

            thenEventually(() -> TestWordAssert.assertThat(getFromApp("WordToBeAdded"))
                    .hasDefinition("A Definition to be added")
                    .containsSynonyms("Synonym1", "Synonym2")
                    .containsExamples("Example1", "Example2"));
        }
    }

    @Nested
    class EditItem {
        @Test
        void editWord() {
            page.navigate(adminAppUrl());
            givenSomeWordsExist();

            givenExists(aWord("WordToBeEdited")
                    .withDefinition("A Definition that should be edited")
                    .withSynonyms(List.of("Synonym1", "Synonym2"))
                    .withExamples(List.of("Example1", "Example2")));

            givenListItemIsClicked();
            thenEventually(aLabel(byText("Page 1 of 3")), isVisible());
            given(textBoxLabelled("Word"), isFilledWith("WordToBeEdited"));
            given(aButton(withName("GO")), isClicked());

            thenEventually(aCell(withText("WordToBeEdited")), isVisible());
            given(aCell(withText("WordToBeEdited")), isClicked());
            given(textBoxLabelled("Meaning"), isFilledWith("A Definition that has been edited"));
            given(textBoxLabelled("Synonyms"), isFilledWith("Synonym1,Synonym2,Synonym3"));
            thenEventually(aButton(withName("SUBMIT")), isVisible());
            when(aButton(withName("SUBMIT")), isClicked());

           thenEventually(() -> TestWordAssert.assertThat(getFromApp("WordToBeEdited"))
                    .hasDefinition("A Definition that has been edited")
                    .containsSynonyms("Synonym1", "Synonym2", "Synonym3")
                    .containsExamples("Example1", "Example2"));
        }
    }
}
