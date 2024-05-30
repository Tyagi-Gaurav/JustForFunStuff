package com.jffs.app

import com.jffs.app.config.DatabaseConfig
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(value = ["com.jffs.app.config"])
open class BeanFactory {
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
        return MongoClient.Factory.create(mongoClientSettings);
    }
}