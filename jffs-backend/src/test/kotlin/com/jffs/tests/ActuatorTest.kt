package com.jffs.tests

import com.jayway.jsonpath.JsonPath.parse
import com.jffs.app.JffsApplication
import com.jffs.tests.initializer.TestContainerDatabaseInitializer
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalManagementPort
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest.newBuilder
import java.net.http.HttpResponse


@ContextConfiguration(initializers = [TestContainerDatabaseInitializer::class])
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JffsApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActuatorTest {

    @LocalManagementPort
    var managementPort: Int = 0

    @Test
    fun statusCheckForApplication() {
        val httpClient = HttpClient.newHttpClient()
        val healthCheckRequest = newBuilder()
            .uri(URI.create("http://localhost:$managementPort/actuator/health"))
            .build();
        val response = httpClient.send(healthCheckRequest, HttpResponse.BodyHandlers.ofString())

        response.statusCode() shouldBe 200
        val parsedJsonResponse = parse(response.body())
        parsedJsonResponse.read("$.status") as String shouldBe "UP"
        parsedJsonResponse.read("$.components.diskSpace.status") as String shouldBe "UP"
    }

    @Test
    fun statusCheckForApplicationShouldIncludeDatabaseCheck() {
        val httpClient = HttpClient.newHttpClient()
        val healthCheckRequest = newBuilder()
            .uri(URI.create("http://localhost:$managementPort/actuator/health"))
            .build();
        val response = httpClient.send(healthCheckRequest, HttpResponse.BodyHandlers.ofString())

        response.statusCode() shouldBe 200
        val parsedJsonResponse = parse(response.body())
        parsedJsonResponse.read("$.status") as String shouldBe "UP"
        parsedJsonResponse.read("$.components.database.status") as String shouldBe "UP"
    }

    @ParameterizedTest
    @ValueSource(strings = ["metrics", "health", "config", "mappings", "info"])
    fun actuatorPrometheusCheck(pathExposedUnderActuator : String) {
        val httpClient = HttpClient.newHttpClient()
        val healthCheckRequest = newBuilder()
            .uri(URI.create("http://localhost:$managementPort/actuator/$pathExposedUnderActuator"))
            .build();
        val response = httpClient.send(healthCheckRequest, HttpResponse.BodyHandlers.ofString())

        response.statusCode() shouldBe 200
    }
}