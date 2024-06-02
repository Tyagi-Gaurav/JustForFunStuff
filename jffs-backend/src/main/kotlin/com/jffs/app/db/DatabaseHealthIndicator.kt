package com.jffs.app.db

import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import org.bson.BsonDocument
import org.bson.BsonInt64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

@Component
class DatabaseHealthIndicator(@Autowired val mongoClient: MongoClient)

    : HealthIndicator {
    override fun health(): Health {
        val database = mongoClient.getDatabase("vocab");
        val dbStats = BsonDocument("dbStats", BsonInt64(1))
        try {
            runBlocking {
                database.runCommand(dbStats)
            }

            return Health.up().build()
        } catch (e: Exception) {
            return Health.down(e).build()
        }
    }
}