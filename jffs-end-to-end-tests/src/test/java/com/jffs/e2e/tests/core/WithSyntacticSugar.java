package com.jffs.e2e.tests.core;

public interface WithSyntacticSugar {
    default <T> T aLabel(T t) {
        return t;
    }

    default <T> T aMenuItem(T t) {
        return t;
    }

    default <T> T anElement(T t) {
        return t;
    }

    default <T> T aLink(T t) {
        return t;
    }

    default <T> T withText(T t) {
        return t;
    }

    default <T> T withName(T t) {
        return t;
    }
}
