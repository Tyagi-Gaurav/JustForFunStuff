package com.jffs.trade.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jffs.trade.core.CustomThreadPoolScheduler;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationPropertiesScan(value = {"com.jffs.trade.oanda"})
public class BeanFactory {
    @Bean
    public CloseableHttpClient closeableHttpClient() {
        final var connConfig = ConnectionConfig.custom()
                .setConnectTimeout(500, TimeUnit.MILLISECONDS)
                .setSocketTimeout(500, TimeUnit.MILLISECONDS)
                .build();

        final var connManager = new BasicHttpClientConnectionManager();
        connManager.setConnectionConfig(connConfig);
        return HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(1);
    }

    @Bean(destroyMethod = "close")
    public CustomThreadPoolScheduler threadPoolScheduler(ScheduledExecutorService executorService) {
        return new CustomThreadPoolScheduler(executorService);
    }
}
