package com.jffs.app.db

import com.jffs.app.config.DatabaseConfig
import com.jffs.app.domain.Word
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository


@Repository
class VocabRepository(
    @Autowired val mongoClient: MongoClient,
    @Autowired val databaseConfig: DatabaseConfig
) {
    suspend fun readAllWords(): List<Word> {
        val database = mongoClient.getDatabase(databaseConfig.dbName);
        val collection = database.getCollection<Word>("word")

        return collection.find().toList().shuffled()
    }
}
