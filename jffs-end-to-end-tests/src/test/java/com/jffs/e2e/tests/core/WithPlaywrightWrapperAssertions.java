package com.jffs.e2e.tests.core;

import com.microsoft.playwright.Locator;

import java.util.function.Consumer;
import java.util.function.Function;

public interface WithPlaywrightWrapperAssertions {
    default Function<Locator, Boolean> isVisible() {
        return Locator::isVisible;
    }

    default Function<Locator, Boolean> isDisabled() {
        return Locator::isDisabled;
    }

    default Function<Locator, Boolean> isEnabled() {
        return Locator::isEnabled;
    }

    default Function<Locator, Boolean> not(Function<Locator, Boolean> function) {
        return locator -> !function.apply(locator);
    }

    default Consumer<Locator> isClicked() {
        return Locator::click;
    }

    default Consumer<Locator> selectsOption(String word) {
        return locator -> locator.selectOption(word);
    }
}
