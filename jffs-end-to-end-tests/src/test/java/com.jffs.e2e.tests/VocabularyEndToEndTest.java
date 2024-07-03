package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static java.lang.Thread.sleep;

class VocabularyEndToEndTest extends AbstractEndToEndTests {

    @Test
    void showErrorWhenNoWordsReceived() {
        page.navigate("http://localhost/games/vocabtesting");

        thenEventually(aLabel(withText("Test your Vocabulary")), isVisible());
        thenEventually(aLabel(withText("Can you think of the meaning before the timer runs out?")), isVisible());

        thenEventually(aButton(withText("Begin")), isVisible());
        thenEventually(aLabel(withText("There seems to be some problem. Please try again later")), not(isVisible()));
        when(aButton(withText("Begin")), isClicked());

        thenEventually(aLabel(withText("There seems to be some problem. Please try again later")), isVisible());
    }

    @Test
    void showWordAfterBeginButtonIsClicked() {
        givenSomeWordsExist();
        page.navigate("http://localhost/games/vocabtesting");

        thenEventually(aLabel(withText("Test your Vocabulary")), isVisible());
        thenEventually(aLabel(withText("Can you think of the meaning before the timer runs out?")), isVisible());

        thenEventually(aButton(withText("Begin")), isVisible());
        when(aButton(withText("Begin")), isClicked());
        thenEventually(aLabel(withText("There seems to be some problem. Please try again later")), not(isVisible()));

        waitFor(Duration.ofSeconds(10));
        thenEventually(anElement(withId("word-text")), isVisible());
        thenEventually(anElement(withId("word-text")), hasValueLike("Word.*"));
    }

    private void waitFor(Duration duration) {
        try {
            sleep(duration.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
