package com.jffs.e2e.tests.core;

import com.microsoft.playwright.Locator;

import java.util.function.Function;

public interface WithPlaywrightWrapperAssertions {
    default Function<Locator, Boolean> isVisible() {
        return (Locator locator) -> locator.isVisible();
    }
}
