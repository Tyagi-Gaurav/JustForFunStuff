package com.jffs.e2e.tests.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jffs.e2e.tests.TestPaginatedWords;
import com.jffs.e2e.tests.TestWord;
import com.jffs.e2e.tests.TestWordBuilder;
import com.jffs.e2e.tests.core.assertion.HttpResponseAssert;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.iterable.Extractor;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jffs.e2e.tests.TestWordBuilder.aWord;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.waitAtMost;

public abstract class AbstractEndToEndTests implements
        WithHTTPSupport,
        WithPlaywrightWrapperAssertions,
        WithSyntacticSugar,
        WithPlaywrightElementProvider,
        WithAdminApp {
    static Playwright playwright;
    private static Browser browser;
    protected Page page;
    HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Duration ASSERTION_TIMEOUT = Duration.ofSeconds(10);

    @BeforeEach
    void setUp() {
        page = browser.newPage();
        page.onConsoleMessage(x -> System.out.println(x.text()));
    }

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    protected void givenExists(TestWordBuilder testWord) {
        try {
            HttpResponse<String> response = httpClient.send(newBuilder()
                    .uri(new URI("http://localhost:9090/admin/v1/words"))
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(testWord.build())))
                    .header("Content-Type", "application/vnd+add.word.v1+json")
                    .build(), ofString());

            assertThat(response.statusCode()).isEqualTo(204);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected HttpResponseAssert assertThatAnHttpCallFor(HttpResponse<String> httpResponse) {
        return HttpResponseAssert.assertThat(httpResponse);
    }

    protected void when(Function<Page, Locator> locatorProvider, Consumer<Locator> action) {
        action.accept(locatorProvider.apply(page));
    }

    protected void and(Function<Page, Locator> locatorProvider, Consumer<Locator> action) {
        action.accept(locatorProvider.apply(page));
    }

    protected void given(Function<Page, Locator> locatorProvider, Consumer<Locator> action) {
        action.accept(locatorProvider.apply(page));
    }

    protected void and(Function<Page, Locator> locatorProvider, Function<Locator, Boolean> evaluator) {
        assertThat(evaluator.apply(locatorProvider.apply(page))).isTrue();
    }

    protected void given(Function<Page, Locator> locatorProvider, Function<Locator, Boolean> evaluator) {
        waitAtMost(ASSERTION_TIMEOUT).until(() -> evaluator.apply(locatorProvider.apply(page)));
    }

    protected void thenEventually(Function<Page, Locator> locatorProvider, Function<Locator, Boolean> evaluator) {
        waitAtMost(ASSERTION_TIMEOUT)
                .until(() -> evaluator.apply(locatorProvider.apply(page)));
    }

    protected <T> void thenEventually(Supplier<T> supplier, Matcher<T> matcher) {
        waitAtMost(ASSERTION_TIMEOUT)
                .until(() -> matcher.matches(supplier.get()));
    }

    protected void givenListItemIsClicked() {
        given(aMenuItem(byText("Vocabulary")), isVisible());
        and(aMenuItem(byText("Vocabulary")), isClicked());

        thenEventually(aMenuItem(byText("List Words")), isVisible());
        and(aMenuItem(byText("List Words")), isClicked());
    }

    protected void givenAddItemIsClicked() {
        given(aMenuItem(byText("Vocabulary")), isVisible());
        and(aMenuItem(byText("Vocabulary")), isClicked());

        thenEventually(aMenuItem(byText("Add Word")), isVisible());
        and(aMenuItem(byText("Add Word")), isClicked());
    }

    @BeforeEach
    void deleteAllWords() throws Exception {
        final var wordsToDelete = new ArrayList<>();
        int page = 1;
        while (page > 0) {
            final var response = aGetRequestWith(adminAppUrlWithPath("admin/v1/words/page/" + page));

            assertThat(response.statusCode()).isEqualTo(200);
            final var testPaginatedWords = objectMapper.readValue(response.body(), TestPaginatedWords.class);
            wordsToDelete.addAll(testPaginatedWords.words().stream().map(TestWord::word).toList());
            page = testPaginatedWords.nextPage();
        }

        wordsToDelete.forEach(word -> {
            try {
                final var response = aDeleteRequestWith(adminAppUrlWithPath("admin/v1/words/" + word));
                assertThat(response.statusCode()).isEqualTo(202);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected void givenSomeWordsExist() {
        for (int i = 1; i <= 25; i++) {
            givenExists(aWord("Word" + i)
                    .withDefinition("Definition" + i)
                    .withSynonyms(List.of("Synonym1" + i, "Synonym2" + i))
                    .withExamples(List.of("Example1" + i, "Example2" + i)));
        }
    }

    protected boolean doesWordExist(String word) throws Exception {
        final var response = aGetRequestWith(adminAppUrlWithPath("admin/v1/words/" + word));
        return response.statusCode() == 200;
    }

    protected TestWord getFromApp(String word) throws Exception {
        final var response = aGetRequestWith(adminAppUrlWithPath("admin/v1/words/" + word));
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        return objectMapper.readValue(response.body(), TestWord.class);
    }

    @AfterEach
    void tearDown() {
        if (page != null) {
            page.close();
        }
    }

    @AfterAll
    static void close() {
        if (browser != null) {
            browser.close();
        }
    }
}
