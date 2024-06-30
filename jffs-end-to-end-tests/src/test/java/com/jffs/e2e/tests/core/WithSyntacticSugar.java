package com.jffs.e2e.tests.core;

public interface WithSyntacticSugar {
    default <T> T withText (T t) {
        return t;
    }

    default <T> T withName (T t) {
        return t;
    }
}
