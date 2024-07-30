package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.jffs.e2e.tests.core.WithJffsApp;
import com.jffs.e2e.tests.core.WithSyntacticSugar;
import org.junit.jupiter.api.Test;

import static com.jffs.e2e.tests.core.assertion.MetricBuilder.aMetric;

class VocabularyEndToEndTest extends AbstractEndToEndTests implements WithJffsApp, WithSyntacticSugar {

    @Test
    void showWordAndItsDataAfterBeginButtonIsClicked() {
        givenSomeWordsExist();
        page.navigate(appUrlWithPath("games/vocabtesting"));

        thenEventually(aLabel(byText("Test your Vocabulary")), isVisible());
        thenEventually(aLabel(byText("Can you think of the meaning before the timer runs out?")), isVisible());

        thenEventually(aButton(withText("Begin")), isVisible());
        when(aButton(withText("Begin")), isClicked());
        thenEventually(aLabel(byText("There seems to be some problem. Please try again later")), not(isVisible()));

        thenEventually(anElement(byTestId("countdown")), isVisible());
        and(anElement(byTestId("word-text")), isVisible());
        and(anElement(byTestId("word-text")), hasValueLike("Word.*"));

        thenEventually(anElement(byTestId("synonym-text")), isVisible());
        and(anElement(byTestId("synonym-text")), hasValueLike("Synonym1.*, Synonym2.*"));

        thenEventually(anElement(byTestId("meanings-text")), isVisible());
        and(anElement(byTestId("meanings-text")), hasValueLike("Definition.*"));

        thenEventually(anElement(byTestId("examples-text")), isVisible());
        and(anElement(byTestId("examples-text")), hasValueLike("Example.*, Example2.*"));

        thenEventually(aButton(withText("Begin")), not(isVisible()));
        thenEventually(aButton(withText("Next")), isVisible());
    }

    @Test
    void metricsGeneratedForApi() {
        givenSomeWordsExist();
        page.navigate(appUrlWithPath("games/vocabtesting"));

        thenEventually(aLabel(byText("Test your Vocabulary")), isVisible());
        thenEventually(aLabel(byText("Can you think of the meaning before the timer runs out?")), isVisible());

        thenEventually(aButton(withText("Begin")), isVisible());
        when(aButton(withText("Begin")), isClicked());

        thenEventually(anElement(byTestId("countdown")), isVisible());
        and(anElement(byTestId("word-text")), isVisible());
        and(anElement(byTestId("word-text")), hasValueLike("Word.*"));

        thenVerifyThat(metrics(), contains(aMetric()
                .labelled("request_latency_seconds")
                .withTag("method", "GET")
                .withTag("path", "/api/v1/words")
                .withTag("quantile", "0.5")
                .isLessThan(1.0)));

        thenVerifyThat(metrics(), contains(aMetric()
                .labelled("response_status_total")
                .withTag("status", "200")
                .isIncrementedBy(2.0)));

        thenVerifyThat(metrics(), contains(aMetric()
                .labelled("request_count_total")
                .withTag("method", "GET")
                .withTag("path", "/api/v1/words")
                .isIncrementedBy(1.0)));
    }
}
