package com.jffs.trade;

public class TestUtils {
    public static long millisElapsedSince(long startTime) {
        return (System.nanoTime() - startTime) / (1000 * 1000);
    }
}
