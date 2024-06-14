package com.jffs.admin.app.db

import com.jffs.admin.app.config.DatabaseConfig
import com.jffs.admin.app.domain.Word
import com.mongodb.client.model.Aggregates.*
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Indexes.descending
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository


@Repository
class VocabRepository(
    @Autowired val mongoClient: MongoClient,
    @Autowired val databaseConfig: DatabaseConfig
) {
    suspend fun readAllWords(pageNum : Int): List<Word> {
        val database = mongoClient.getDatabase(databaseConfig.dbName);
        val collection = database.getCollection<Word>("word")

        val pipeline = listOf(
//            search(text(fieldPath("word"), "{ \$eq: Staunch }"), searchOptions().index("word_name_index")), -- WORKS
            match(Filters.regex("word", ".*")),
            sort(descending("_id")),
            skip((pageNum - 1) * 10),
            limit(10),
            project(
                fields(
                    listOf(
                        include("word", "meanings", "synonyms", "examples", "modifiedDateTime", "score"),
                        Projections.meta("paginationToken", "searchSequenceToken")
                    )
                )
            )
        )
        val result = collection.aggregate(pipeline).map { value -> value }.toList()
        println(result)
        return result
    }
}