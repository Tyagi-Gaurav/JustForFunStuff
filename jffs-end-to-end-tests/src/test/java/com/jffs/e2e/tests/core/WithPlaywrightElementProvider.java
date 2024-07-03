package com.jffs.e2e.tests.core;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.function.Function;

public interface WithPlaywrightElementProvider {
    default Function<Page, Locator> aLabel(String text) {
        return (page) -> page.getByText(text);
    }

    default Function<Page, Locator> anElement(String testId) {
        return (page) -> page.getByTestId(testId);
    }

    default Function<Page, Locator> aButton(String buttonText) {
        return (page) -> page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(buttonText));
    }

    default Function<Page, Locator> aMenuItem(String menuText) {
        return (page) -> page.getByText(menuText);
    }
}