package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.jffs.e2e.tests.core.WithJffsApp;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;

class VocabularyEndToEndTest extends AbstractEndToEndTests implements WithJffsApp {

    @Test
    void showWordAfterBeginButtonIsClicked() {
        givenSomeWordsExist();
        page.navigate(appUrlWithPath("games/vocabtesting"));

        thenEventually(aLabel(byText("Test your Vocabulary")), isVisible());
        thenEventually(aLabel(byText("Can you think of the meaning before the timer runs out?")), isVisible());

        thenEventually(aButton(withText("Begin")), isVisible());
        when(aButton(withText("Begin")), isClicked());
        thenEventually(aLabel(byText("There seems to be some problem. Please try again later")), not(isVisible()));

        thenEventually(anElement(byTestId("countdown")), isVisible());
        thenEventually(anElement(byTestId("word-text")), isVisible());
        thenEventually(anElement(byTestId("word-text")), hasValueLike("Word.*"));
    }
}
