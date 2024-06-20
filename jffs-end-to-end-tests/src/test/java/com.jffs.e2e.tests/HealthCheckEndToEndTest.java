package com.jffs.e2e.tests;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class HealthCheckEndToEndTest extends AbstractEndToEndTests {
    @Test
    void healthCheckJffsAdminBackendApp() throws URISyntaxException, IOException, InterruptedException {
        assertThat(statusCodeFrom(aGetRequest("http://localhost:9091/actuator/health")))
                .isEqualTo(200);
    }

    @Test
    void healthCheckJffsBackendApp() throws URISyntaxException, IOException, InterruptedException {
        assertThat(statusCodeFrom(aGetRequest("http://localhost:8081/actuator/health")))
                .isEqualTo(200);
    }
}
