package com.jffs.trade.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

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

    @Bean(destroyMethod = "stop")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        final var threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        return threadPoolTaskScheduler;
    }
}
