package com.jffs.e2e.tests.core.assertion;

import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MetricBuilder {
    private String metricName;
    private List<Pair<String, String>> tags = new ArrayList<>();
    private List<Predicate<Double>> predicates = new ArrayList<>();

    private MetricBuilder() {
    }

    public static MetricBuilder aMetric() {
        return new MetricBuilder();
    }

    public MetricBuilder labelled(String metricName) {
        this.metricName = metricName;
        return this;
    }

    public MetricBuilder withTag(String tagName, String tagValue) {
        tags.add(new Pair<>(tagName, tagValue));
        return this;
    }

    public MetricBuilder isIncrementedBy(double value) {
        predicates.add((metricValue) -> metricValue == value);
        return this;
    }

    public Metric build() {
        return new Metric(metricName, tags, predicates);
    }

    public MetricBuilder isLessThan(double value) {
        predicates.add((metricValue) -> metricValue < value);
        return this;
    }
}