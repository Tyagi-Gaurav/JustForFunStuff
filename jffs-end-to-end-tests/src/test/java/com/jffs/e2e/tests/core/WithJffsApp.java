package com.jffs.e2e.tests.core;

public interface WithJffsApp {
    default String appUrl() {
        return "http://localhost";
    }

    default String appUrlWithPath(String path) {
        return String.format("%s/%s", appUrl(), path);
    }

    default String appMgtUrlWithPath(String path) {
        return String.format("%s/%s", mgtUrl(), path);
    }

    default String mgtUrl() {
        return "http://localhost:8081";
    }
}