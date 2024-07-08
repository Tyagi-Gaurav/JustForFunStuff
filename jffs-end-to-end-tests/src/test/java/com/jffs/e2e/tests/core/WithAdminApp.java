package com.jffs.e2e.tests.core;

import com.jffs.e2e.tests.TestWord;

import java.net.http.HttpClient;

public interface WithAdminApp {
    default String adminAppUrl() {
        return "http://localhost:8060";
    }

    default String adminMgtUrl() {
        return "http://localhost:9091";
    }

    default String adminAppUrlWithPath(String path) {
        return String.format("%s/%s", adminAppUrl(), path);
    }

    default String adminMgtUrlWithPath(String path) {
        return String.format("%s/%s", adminMgtUrl(), path);
    }
}