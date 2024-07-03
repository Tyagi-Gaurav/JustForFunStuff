package com.jffs.e2e.tests;

import com.jffs.e2e.tests.core.AbstractEndToEndTests;
import com.jffs.e2e.tests.core.WithJffsApp;
import org.junit.jupiter.api.Test;

import static com.jffs.e2e.tests.core.builder.JsonXPathMatcherBuilder.aJsonMessage;
import static org.hamcrest.Matchers.equalTo;

class HealthCheckEndToEndTest extends AbstractEndToEndTests implements WithJffsApp {
    @Test
    void healthCheckJffsAdminBackendApp() throws Exception {
        assertThatAnHttpCallFor(aGetRequestWith(adminMgtUrlWithPath("actuator/health")))
                .hasStatusCode(200)
                .withBody(aJsonMessage()
                        .withFieldWithStringValue("status", equalTo("UP"))
                        .withFieldWithStringValue("components.diskSpace.status", equalTo("UP"))
                        .withFieldWithStringValue("components.ping.status", equalTo("UP")));
    }

    @Test
    void healthCheckJffsBackendApp() throws Exception {
        assertThatAnHttpCallFor(aGetRequestWith(appMgtUrlWithPath("actuator/health")))
                .hasStatusCode(200)
                .withBody(aJsonMessage()
                        .withFieldWithStringValue("status", equalTo("UP"))
                        .withFieldWithStringValue("components.diskSpace.status", equalTo("UP"))
                        .withFieldWithStringValue("components.database.status", equalTo("UP"))
                        .withFieldWithStringValue("components.ping.status", equalTo("UP")));
    }
}
