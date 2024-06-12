package com.jffs.admin.app.db

import com.jffs.admin.app.domain.Word
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class VocabRepository(@Autowired val mongoClient: MongoClient) {
    suspend fun readAllWords(): List<Word> {
        val database = mongoClient.getDatabase("vocab");
        val collection = database.getCollection<Word>("word")

        return collection.find().toList().shuffled()
    }
}