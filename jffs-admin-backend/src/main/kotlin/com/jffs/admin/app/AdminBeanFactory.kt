package com.jffs.admin.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jffs.admin.app.config.DatabaseConfig
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
@ConfigurationPropertiesScan(value = ["com.jffs.admin.app.config"])
open class AdminBeanFactory {
    @Bean
    open fun mongoDbClient(databaseConfig: DatabaseConfig): MongoClient {
        val connectionString = databaseConfig.connectionString()
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .serverApi(serverApi)
            .build()
        return MongoClient.Factory.create(mongoClientSettings);
    }

    @Bean
    @Primary
    open fun objectMapper() = ObjectMapper()
        .registerKotlinModule()
}