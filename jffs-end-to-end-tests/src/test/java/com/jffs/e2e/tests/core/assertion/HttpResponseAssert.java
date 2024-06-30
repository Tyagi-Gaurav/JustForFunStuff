package com.jffs.e2e.tests.core.assertion;

import com.jayway.jsonpath.JsonPath;
import com.jffs.e2e.tests.core.builder.JsonXPathMatcherBuilder;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.net.http.HttpResponse;

public class HttpResponseAssert extends AbstractAssert<HttpResponseAssert, HttpResponse<String>> {

    protected HttpResponseAssert(HttpResponse httpResponse, Class<?> selfType) {
        super(httpResponse, selfType);
    }

    public static HttpResponseAssert assertThat(HttpResponse<String> actual) {
        return new HttpResponseAssert(actual, HttpResponseAssert.class);
    }

    public HttpResponseAssert hasStatusCode(int statusCode) {
        Assertions.assertThat(this.actual.statusCode()).isEqualTo(statusCode);
        return this;
    }

    public HttpResponseAssert withBody(JsonXPathMatcherBuilder jsonXPathMatcherBuilder) {
        final var conditions = jsonXPathMatcherBuilder.build();
        final var inputJson = JsonPath.parse(this.actual.body());
        Assertions.assertThat(conditions)
                .isNotEmpty()
                .allMatch(documentContextHamcrestCondition -> documentContextHamcrestCondition.matches(inputJson));
        return this;
    }
}