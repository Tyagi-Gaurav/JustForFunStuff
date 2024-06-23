package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HealthCheckEndToEndTest extends AbstractEndToEndTests {
    @Test
    void healthCheckJffsAdminBackendApp() throws Exception {
        assertThat(statusCodeFrom(aGetRequest("http://localhost:9091/actuator/health")))
                .isEqualTo(200);
    }

    @Test
    void healthCheckJffsBackendApp() throws Exception {
        assertThat(statusCodeFrom(aGetRequest("http://localhost:8081/actuator/health")))
                .isEqualTo(200);
    }
}
