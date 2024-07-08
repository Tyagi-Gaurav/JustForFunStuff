package com.jffs.e2e.tests;

import java.util.List;

public class TestWordBuilder {
    private String definition;
    private String word;
    private List<String> synonyms = List.of();
    private List<String> examples;

    private TestWordBuilder(String word) {
        this.word = word;
    }

    public static TestWordBuilder aWord(String word) {
        return new TestWordBuilder(word);
    }

    public TestWordBuilder withDefinition(String definition) {
        this.definition = definition;
        return this;
    }

    public TestWordBuilder withSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
        return this;
    }

    public TestWordBuilder withExamples(List<String> examples) {
        this.examples = examples;
        return this;
    }

    public TestWord build() {
        return new TestWord(word, List.of(new TestWord.TestMeaning(definition, synonyms, examples)));
    }
}
