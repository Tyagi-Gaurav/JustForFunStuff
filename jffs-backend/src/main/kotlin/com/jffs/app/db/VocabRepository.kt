package com.jffs.app.db

import com.jffs.app.domain.Word
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository


@Repository
class VocabRepository(@Autowired val mongoClient : MongoClient) {
    suspend fun access() {
        val database = mongoClient.getDatabase("vocab");
        val collection = database.getCollection<Word>("word")

        val countDocuments = collection.countDocuments();
        println("Document count $countDocuments")
    }
}

