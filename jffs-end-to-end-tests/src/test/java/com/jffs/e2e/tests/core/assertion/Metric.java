package com.jffs.e2e.tests.core.assertion;

import kotlin.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public record Metric(String metricName,
                     List<Pair<String, String>> tags,
                     List<Predicate<Double>> predicates) {
    public String getFullMetricName() {
        final var tagBuilder = new StringBuilder();
        tags.sort(Comparator.comparing(Pair::component1));
        for (int i = 0; i < tags.size(); i++) {
            tagBuilder.append(tags.get(i).component1()).append("=").append("\"").append(tags.get(i).component2()).append("\"");
            if (!(i+1 >= tags.size())) {
                tagBuilder.append(",");
            }
        }

        final var metricBuilder = new StringBuilder();
        metricBuilder.append(metricName);
        if (!tagBuilder.isEmpty()) {
            metricBuilder.append("{").append(tagBuilder).append("}");
        }

        return metricBuilder.toString();
    }
}