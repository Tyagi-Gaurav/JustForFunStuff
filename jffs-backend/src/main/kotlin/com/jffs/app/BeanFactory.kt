package com.jffs.app

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BeanFactory {
    @Bean
    open fun mongoDbClient(): MongoClient {
        val connectionString =
            "mongodb+srv://jffs-prod-user:dDsv13DNGbeomU8i@prod.0nv9qyd.mongodb.net/?retryWrites=true&w=majority&appName=Prod"
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