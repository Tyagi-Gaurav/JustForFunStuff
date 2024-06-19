package com.jffs.admin.app.db

import com.jffs.admin.app.config.DatabaseConfig
import com.jffs.admin.app.domain.PaginatedWords
import com.jffs.admin.app.domain.Word
import com.mongodb.client.model.Aggregates.*
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Indexes.descending
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


@Repository
class AdminRepository(
    @Autowired val mongoClient: MongoClient,
    @Autowired val databaseConfig: DatabaseConfig
) {
    suspend fun readAllWords(pageNum: Int): PaginatedWords {
        val database = mongoClient.getDatabase(databaseConfig.dbName);
        val collection = database.getCollection<Word>("word")
        val limit = 10

        val totalRecords = collection.find().count()
        val totalPages = totalRecords / limit + Math.min(1, (totalRecords % limit))

        val nextPage = if (pageNum + 1 > totalPages) -1 else pageNum + 1
        val previousPage = if (pageNum == 1) -1 else pageNum - 1

        val pipeline = listOf(
            match(regex("word", ".*")),
            sort(descending("_id")),
            skip((pageNum - 1) * 10),
            limit(limit),
            project(
                fields(
                    listOf(
                        include("word", "meanings", "synonyms", "examples")
                    )
                )
            )
        )

        val words = collection.aggregate(pipeline).map { value -> value }.toList()
        return PaginatedWords(words, totalPages, nextPage, previousPage, pageNum)
    }

    suspend fun findByWord(word: String) : Word? {
        val database = mongoClient.getDatabase(databaseConfig.dbName);
        val collection = database.getCollection<Word>("word")

        return collection.find<Word>(regex("word", ".*$word.*")).firstOrNull()
    }

    suspend fun findBySynonym(synonym: String) : Word? {
        val database = mongoClient.getDatabase(databaseConfig.dbName);
        val collection = database.getCollection<Word>("word")

        return collection.find<Word>(regex("meanings.0.synonyms", ".*$synonym.*")).firstOrNull()
    }

    suspend fun update(word: String, newWord: Word) {
        val database = mongoClient.getDatabase(databaseConfig.dbName);
        val collection = database.getCollection<Word>("word")

        collection.updateOne(eq("word", word),
            listOf(Updates.combine(
                Updates.set("word", newWord.word),
                Updates.set("meanings", newWord.meanings),
                Updates.set("modifiedDateTime", LocalDateTime.now())
            ))
        )
    }

    suspend fun add(word: Word) {
        val database = mongoClient.getDatabase(databaseConfig.dbName);
        val collection = database.getCollection<Word>("word")

        collection.insertOne(word)
    }

    suspend fun delete(word: String) {
        val database = mongoClient.getDatabase(databaseConfig.dbName);
        val collection = database.getCollection<Word>("word")

        collection.deleteOne(eq("word", word))
    }
}