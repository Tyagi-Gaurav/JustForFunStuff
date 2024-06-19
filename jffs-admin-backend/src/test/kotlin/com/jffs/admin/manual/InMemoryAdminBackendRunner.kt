package com.jffs.admin.manual

import com.jffs.admin.app.db.AdminRepository
import com.jffs.admin.app.domain.Meaning
import com.jffs.admin.app.domain.Word
import com.jffs.admin.app.tests.initializer.TestContainerDatabaseInitializer
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Controller
import java.time.LocalDateTime

@SpringBootApplication
@EnableAutoConfiguration(exclude = [MongoAutoConfiguration::class, MongoReactiveAutoConfiguration::class])
@ComponentScan("com.jffs.admin.app")
open class InMemoryAdminBackendRunner


fun main() {
    SpringApplicationBuilder(InMemoryAdminBackendRunner::class.java)
        .initializers(TestContainerDatabaseInitializer())
        .build()
        .run()
}

@Controller
class AdminInMemoryTestController(@Autowired
                                  val adminRepository: AdminRepository,
                                  @Autowired
                                  val mongoClient: MongoClient) {

    init {
        val database = mongoClient?.getDatabase("testDB")
        val collection = database?.getCollection<Word>("word")
        runBlocking {
            collection?.insertMany(
                generateListOfWords(25)
            )
            println("Total number of words : ${collection?.find()?.count()}")
        }
    }

    private fun generateListOfWords(wordsCount: Int): List<Word> {
        val words = mutableListOf<Word>()
        for (index in 1..wordsCount) {
            words.add(
                Word(
                    "AWord$index",
                    listOf(Meaning("A definition 1$index; A definition 2$index", listOf("synonym1$index", "synonym2$index", "synonym3$index"), listOf("example$index"))),
                    LocalDateTime.now()
                )
            )
        }
        return words.toList()
    }
}

