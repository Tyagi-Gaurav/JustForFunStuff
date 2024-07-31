package com.jffs.e2e.tests.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jffs.e2e.tests.TestPaginatedWords;
import com.jffs.e2e.tests.TestWord;
import com.jffs.e2e.tests.TestWordBuilder;
import com.jffs.e2e.tests.core.assertion.HttpResponseAssert;
import com.jffs.e2e.tests.core.assertion.Metric;
import com.jffs.e2e.tests.core.assertion.MetricBuilder;
import com.microsoft.playwright.*;
import kotlin.Pair;
import org.assertj.core.api.Assertions;
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
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        WithAdminApp,
        WithJffsApp {
    static Playwright playwright;
    private static Browser browser;
    protected Page page;
    HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Duration ASSERTION_TIMEOUT = Duration.ofSeconds(10);
    protected Map<String, Double> beforeMetrics = Collections.emptyMap();

    @BeforeEach
    void setUp() {
        page = browser.newPage();
        page.onConsoleMessage(x -> System.out.println(x.text()));
    }

    @BeforeEach
    void captureBeforeMetrics() throws Exception {
        beforeMetrics = captureMetrics();
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

    protected void thenEventually(Runnable runnable) {
        waitAtMost(ASSERTION_TIMEOUT)
                .until(() -> {
                    runnable.run();
                    return true;
                });
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

    protected TestWord getFromApp(String word) {
        try {
            final var response = aGetRequestWith(adminAppUrlWithPath("admin/v1/words/" + word));
            Assertions.assertThat(response.statusCode()).isEqualTo(200);
            return objectMapper.readValue(response.body(), TestWord.class);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Map<String, Double> captureMetrics() throws Exception {
        HttpResponse<String> stringHttpResponse = aGetRequestWith(appMgtUrlWithPath("actuator/prometheus"));
        String body = stringHttpResponse.body();
        return Arrays.stream(body.split("\n"))
                .filter(line -> !line.startsWith("#"))
                .map(line -> {
                    int splitterIndex = line.lastIndexOf("}");
                    if (splitterIndex == -1) {
                        splitterIndex = line.lastIndexOf(" ");
                    }
                    String metric = line.substring(0, splitterIndex + 1);
                    return new Pair<>(metric, Double.parseDouble(line.substring(splitterIndex + 1).trim()));
                })
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    protected void thenVerifyThat(Extractor<Map<String, Double>> extractor, MetricBuilder metricBuilder) {
        Metric expectedMetrics = metricBuilder.build();
        verify(expectedMetrics, extractor.extract());
    }

    private void verify(Metric expectedMetric, Map<String, Double> actual) {
        String fullMetricName = expectedMetric.getFullMetricName();
        assertThat(actual).containsKey(fullMetricName);

        Double actualValue = actual.get(fullMetricName);
        assertThat(expectedMetric.predicates()
                .stream()
                .allMatch(predicate -> predicate.test(actualValue))).isTrue();
    }

    protected Extractor<Map<String, Double>> metrics() {
        return () -> {
            try {
                Map<String, Double> afterMetrics = captureMetrics();
                return afterMetrics.entrySet()
                        .stream()
                        .map(entry -> {
                            if (beforeMetrics.containsKey(entry.getKey())) {
                                return Map.of(entry.getKey(), entry.getValue() - beforeMetrics.get(entry.getKey()));
                            } else {
                                return Map.of(entry.getKey(), entry.getValue());
                            }
                        }).reduce((map1, map2) -> {
                            Map<String, Double> objectObjectHashMap = new HashMap<>();
                            objectObjectHashMap.putAll(map1);
                            objectObjectHashMap.putAll(map2);
                            return objectObjectHashMap;
                        }).orElse(Map.of());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
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
