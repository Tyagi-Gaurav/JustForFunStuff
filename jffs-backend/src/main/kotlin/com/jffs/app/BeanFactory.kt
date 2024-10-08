package com.jffs.app

import com.jffs.app.config.DatabaseConfig
import com.jffs.app.metrics.EndpointLatencyTimer
import com.jffs.app.metrics.EndpointRequestCounter
import com.jffs.app.metrics.EndpointResponseStatusCounter
import com.jffs.app.metrics.UIEventCounter
import com.jffs.app.resource.domain.UIEvent
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(value = ["com.jffs.app.config"])
open class BeanFactory() {

    @Bean
    open fun mongoDbClient(databaseConfig: DatabaseConfig): MongoClient {
        val connectionString = databaseConfig.connectionString();
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .serverApi(serverApi)
            .build()
        return MongoClient.Factory.create(mongoClientSettings)
    }

    @Bean
    open fun uiEventCounter(meterRegistry: MeterRegistry) : UIEventCounter {
        return UIEventCounter(meterRegistry)
    }

    @Bean
    open fun endpointRequestCounter(meterRegistry: MeterRegistry) : EndpointRequestCounter {
        return EndpointRequestCounter(meterRegistry)
    }

    @Bean
    open fun endpointMetrics(meterRegistry: MeterRegistry) : EndpointLatencyTimer {
        return EndpointLatencyTimer(meterRegistry)
    }

    @Bean
    open fun endpoinStatus(meterRegistry: MeterRegistry) : EndpointResponseStatusCounter {
        return EndpointResponseStatusCounter(meterRegistry)
    }

    @Bean
    open fun meterRegistry() : MeterRegistry {
        return PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    }
}