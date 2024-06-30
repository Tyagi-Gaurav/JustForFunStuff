package com.jffs.e2e.tests.core.builder;

import com.jayway.jsonpath.DocumentContext;
import org.assertj.core.api.HamcrestCondition;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;

public class JsonXPathMatcherBuilder {
    List<XPathField> xPathFieldsMatchers = new ArrayList<>();

    public static JsonXPathMatcherBuilder aJsonMessage() {
        return new JsonXPathMatcherBuilder();
    }

    public JsonXPathMatcherBuilder withFieldWithStringValue(String xpath, Matcher<String> value) {
        xPathFieldsMatchers.add(new XPathField(xpath, value));
        return this;
    }

    public List<HamcrestCondition<DocumentContext>> build() {
        return xPathFieldsMatchers
                .stream()
                .map(xPathField ->
                        new HamcrestCondition<>(new BaseMatcher<DocumentContext>() {
                            @Override
                            public boolean matches(Object o) {
                                DocumentContext jsonContext = (DocumentContext) o;
                                return xPathField.stringMatcher.matches(jsonContext.read(xPathField.fieldXPath));
                            }

                            public void describeTo(Description description) {
                                description.appendText(xPathField.fieldXPath);
                            }
                        }))
                .toList();
    }

    private record XPathField(String fieldXPath, Matcher<String> stringMatcher) {
    }
}
