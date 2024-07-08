package com.jffs.e2e.tests.core.assertion;

import com.jffs.e2e.tests.TestWord;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Arrays;

public class TestWordAssert extends AbstractAssert<TestWordAssert, TestWord> {
    protected TestWordAssert(TestWord testWord, Class<?> selfType) {
        super(testWord, selfType);
    }

    public static TestWordAssert assertThat(TestWord testWord) {
        return new TestWordAssert(testWord, TestWordAssert.class);
    }

    public TestWordAssert hasDefinition(String definition) {
        Assertions.assertThat(this.actual.meanings().get(0).definition()).isEqualTo(definition);
        return this;
    }

    public TestWordAssert containsSynonyms(String... synonyms) {
        Assertions.assertThat(this.actual.meanings().get(0).synonyms()).isEqualTo(Arrays.asList(synonyms));
        return this;
    }

    public TestWordAssert containsExamples(String... examples) {
        Assertions.assertThat(this.actual.meanings().get(0).examples()).isEqualTo(Arrays.asList(examples));
        return this;
    }
}
