package com.jffs.e2e.tests;

import java.util.List;

public record TestWord(String word, List<TestMeaning> meanings) {
    public record TestMeaning(String definition, List<String> synonyms, List<String> examples) {
    }
}

