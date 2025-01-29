package com.jffs.trade.domain;

import java.util.List;
import java.util.Map;

public record EconomicDependencies(Map<String, Correlation> economicDependencies) {

    public List<String> getDirectCorrelationFor(String economicFactor) {
        Correlation correlation = economicDependencies.get(economicFactor);
        return correlation.direct();
    }

    public List<String> getInverseCorrelationFor(String economicFactor) {
        Correlation correlation = economicDependencies.get(economicFactor);
        return correlation.inverse();
    }
}
