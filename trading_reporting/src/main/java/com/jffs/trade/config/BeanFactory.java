package com.jffs.trade.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jffs.trade.core.CustomThreadPoolScheduler;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.util.DriverDataSource;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationPropertiesScan(basePackages = {"com.jffs.trade.client"})
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

    @Bean
    public DataSource hikariDataSOurce() {
        File databaseFile = new File("trading_reporting/reporting.db");
        if(!databaseFile.exists()) {
            throw new RuntimeException("Failed to find database: " + databaseFile);
        }
        DataSource sqliteDataSource = new DriverDataSource("jdbc:sqlite:" + databaseFile.getAbsolutePath(),
                "org.sqlite.JDBC",
                new Properties(), null, null);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("SQLiteConnectionPool");
        hikariConfig.setDataSourceClassName("org.sqlite.SQLiteDataSource");
        hikariConfig.setDataSource(sqliteDataSource);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public DSLContext dslContext(DataSource dataSource) {
        return DSL.using(dataSource, SQLDialect.SQLITE);
    }
}
