package com.jffs.e2e.tests.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jffs.e2e.tests.TestPaginatedWords;
import com.jffs.e2e.tests.TestWord;
import com.jffs.e2e.tests.TestWordBuilder;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.waitAtMost;

public abstract class AbstractEndToEndTests implements WithHTTPSupport, WithPlaywrightWrapperAssertions {
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

    protected void thenEventually(Supplier<Locator> locatorSupplier, Function<Locator, Boolean> evaluator) {
        waitAtMost(ASSERTION_TIMEOUT)
                .until(() -> evaluator.apply(locatorSupplier.get()));
    }


    @BeforeEach
    void deleteAllWords() throws Exception {
        final var wordsToDelete = new ArrayList<>();
        int page = 1;
        while (page > 0) {
            final var response = aGetRequest("http://localhost:9090/admin/v1/words/page/" + page);

            assertThat(response.statusCode()).isEqualTo(200);
            final var testPaginatedWords = objectMapper.readValue(response.body(), TestPaginatedWords.class);
            wordsToDelete.addAll(testPaginatedWords.words().stream().map(TestWord::word).toList());
            page = testPaginatedWords.nextPage();
        }

        wordsToDelete.forEach(word -> {
            try {
                final var response = aDeleteRequest("http://localhost:9090/admin/v1/words/" + word);
                assertThat(response.statusCode()).isEqualTo(202);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
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
