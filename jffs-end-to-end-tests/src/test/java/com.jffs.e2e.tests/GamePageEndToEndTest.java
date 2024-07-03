package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.jffs.e2e.tests.core.WithJffsApp;
import org.junit.jupiter.api.Test;

class GamePageEndToEndTest extends AbstractEndToEndTests implements WithJffsApp {
    @Test
    void navigatesToTicTacToePage() {
        page.navigate(appUrlWithPath("games"));
        given(aLink(byAltText("noughts_crosses")), isVisible());
        and(aLink(byAltText("noughts_crosses")), isClicked());
        thenEventually(aLabel(byText("Next Player to Play: X")), isVisible());
    }

    @Test
    void navigatesToVocabularyPage() {
        page.navigate(appUrlWithPath("games"));
        given(aLink(byAltText("vocab_test")), isVisible());
        and(aLink(byAltText("vocab_test")), isClicked());
        thenEventually(aLabel(byText("Test your Vocabulary")), isVisible());
    }
}
