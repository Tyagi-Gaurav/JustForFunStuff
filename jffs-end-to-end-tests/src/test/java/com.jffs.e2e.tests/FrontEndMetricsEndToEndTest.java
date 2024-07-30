package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.jffs.e2e.tests.core.WithSyntacticSugar;
import org.junit.jupiter.api.Test;

import static com.jffs.e2e.tests.core.assertion.MetricBuilder.aMetric;

class FrontEndMetricsEndToEndTest extends AbstractEndToEndTests implements WithSyntacticSugar {
    @Test
    void incrementTicTacToeMetricsWhenPageAccessed() {
        page.navigate(appUrlWithPath("games"));

        given(aLink(byAltText("noughts_crosses")), isVisible());
        and(aLink(byAltText("noughts_crosses")), isClicked());

        thenVerifyThat(metrics(), contains(aMetric()
                .labelled("request_count_total")
                .withTag("method", "POST")
                .withTag("path", "/api/v1/ui/event")
                .isIncrementedBy(1.0)));

        thenVerifyThat(metrics(), contains(aMetric()
                .labelled("ui_event_total")
                .withTag("page", "Game")
                .withTag("component", "TicTacToe")
                .withTag("action", "Card-Clicked")
                .isIncrementedBy(1.0)));
    }
}
