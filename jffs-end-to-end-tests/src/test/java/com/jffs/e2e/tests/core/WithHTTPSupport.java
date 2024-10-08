package com.jffs.e2e.tests.core;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

public interface WithHTTPSupport {
    HttpClient httpClient = HttpClient.newHttpClient();

    default HttpResponse<String> aGetRequestWith(String url) throws Exception {
        return httpClient.send(newBuilder()
                .uri(new URI(url))
                .GET()
                .build(), ofString());
    }

    default HttpResponse<String> aDeleteRequestWith(String url) throws Exception {
        return httpClient.send(newBuilder()
                .uri(new URI(url))
                .DELETE()
                .build(), ofString());
    }

    default <T> int statusCodeFrom(HttpResponse<T> httpResponse) {
        return httpResponse.statusCode();
    }
}
