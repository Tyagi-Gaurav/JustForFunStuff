package com.jffs.trade.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class Graph {

    private final EconomicDependencies dependencies;

    public Graph() {
        ObjectMapper objectMapper = new ObjectMapper();
        try(InputStream resourceAsStream = Graph.class.getResourceAsStream("/economicDependencies.json")) {
            dependencies = objectMapper.readValue(resourceAsStream,
                    EconomicDependencies.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        final var graph = new Graph();
//        graph.calculateImpact("USD", "STRONG");
//        graph.calculateImpact("USD", "WEAK");
    }

    private void calculateImpact(String economicFactor, String signal) {
        if ("STRONG".equalsIgnoreCase(signal)) {
            System.out.println(dependencies.getDirectCorrelationFor(economicFactor));
        } else {
            System.out.println(dependencies.getInverseCorrelationFor(economicFactor));
        }
    }

}
