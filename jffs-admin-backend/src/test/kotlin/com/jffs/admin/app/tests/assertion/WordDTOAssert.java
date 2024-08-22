package com.jffs.admin.app.tests.assertion;

import com.jffs.admin.app.resource.WordDTO;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Arrays;

public class WordDTOAssert extends AbstractAssert<WordDTOAssert, WordDTO> {
    protected WordDTOAssert(WordDTO testWord, Class<?> selfType) {
        super(testWord, selfType);
    }

    public static WordDTOAssert assertThat(WordDTO testWord) {
        return new WordDTOAssert(testWord, WordDTOAssert.class);
    }

    public WordDTOAssert hasDefinition(String definition) {
        Assertions.assertThat(this.actual.meanings().get(0).definition()).isEqualTo(definition);
        return this;
    }

    public WordDTOAssert isWord(String word) {
        Assertions.assertThat(this.actual.getWord()).isEqualTo(word);
        return this;
    }

    public WordDTOAssert containsSynonyms(String... synonyms) {
        Assertions.assertThat(this.actual.meanings().get(0).synonyms()).isEqualTo(Arrays.asList(synonyms));
        return this;
    }

    public WordDTOAssert containsExamples(String... examples) {
        Assertions.assertThat(this.actual.meanings().get(0).examples()).isEqualTo(Arrays.asList(examples));
        return this;
    }
}