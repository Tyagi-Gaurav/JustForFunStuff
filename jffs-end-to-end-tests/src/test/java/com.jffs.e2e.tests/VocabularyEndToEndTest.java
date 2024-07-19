package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.jffs.e2e.tests.core.WithJffsApp;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

class VocabularyEndToEndTest extends AbstractEndToEndTests implements WithJffsApp {

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
    void metricsGeneratedForApi() throws Exception {
        givenSomeWordsExist();
        page.navigate(appUrlWithPath("games/vocabtesting"));

        thenEventually(aLabel(byText("Test your Vocabulary")), isVisible());
        thenEventually(aLabel(byText("Can you think of the meaning before the timer runs out?")), isVisible());

        thenEventually(aButton(withText("Begin")), isVisible());
        when(aButton(withText("Begin")), isClicked());

        thenEventually(anElement(byTestId("countdown")), isVisible());
        and(anElement(byTestId("word-text")), isVisible());
        and(anElement(byTestId("word-text")), hasValueLike("Word.*"));

        checkMetricExists("request_latency_seconds", "method", "GET", "path", "/api/v1/words", "quantile", "0.5");
        checkMetricExists("response_status_total", "status", "200");
        checkMetricExists("request_count_total", "method", "GET", "path", "/api/v1/words");
    }

    private void checkMetricExists(String metricName, String... tags) throws Exception {
        final var tagBuilder = new StringBuilder();
        for (int i = 0; i < tags.length; i+=2) {
            tagBuilder.append(tags[i]).append("=").append("\"").append(tags[i + 1]).append("\"");
            if (!(i+2 >= tags.length)) {
                tagBuilder.append(",");
            }
        }

        final var metricBuilder = new StringBuilder();
        metricBuilder.append(metricName);
        if (!tagBuilder.isEmpty()) {
            metricBuilder.append("{").append(tagBuilder).append("}");
        }

        Map<String, Double> afterMetrics = captureMetrics();
        assertThat(afterMetrics).containsKey(metricBuilder.toString());
    }
}
