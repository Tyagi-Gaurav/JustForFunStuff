package com.jffs.admin.app

import com.jffs.admin.app.db.AdminRepository
import com.jffs.admin.app.domain.Meaning
import com.jffs.admin.app.domain.Word
import com.jffs.admin.app.resource.MeaningDTO
import com.jffs.admin.app.resource.PaginatedWordsDTO
import com.jffs.admin.app.resource.WordDTO
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
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @GetMapping("/v1/words/{pageNum}")
    suspend fun words(@PathVariable("pageNum") pageNum : String): ResponseEntity<PaginatedWordsDTO> {
        println ("received request")
        val paginatedWords = adminRepository.readAllWords(Integer.parseInt(pageNum))
        val words = paginatedWords.words
            .map { word ->
                WordDTO(
                    word.word,
                    word.meanings.map { meaning: Meaning ->
                        MeaningDTO(
                            meaning.definition,
                            meaning.synonyms,
                            meaning.examples
                        )
                    })
            }.toList()
        val response = PaginatedWordsDTO(words,
            paginatedWords.totalPages,
            paginatedWords.currentPage,
            paginatedWords.nextPage,
            paginatedWords.previousPage,
        )
        return ResponseEntity.ok(response)
    }

    private fun generateListOfWords(wordsCount: Int): List<Word> {
        val words = mutableListOf<Word>()
        for (index in 1..wordsCount) {
            words.add(
                Word(
                    "A Word $index",
                    listOf(Meaning("A definition $index", listOf("synonym$index"), listOf("example$index"))),
                    LocalDateTime.now()
                )
            )
        }
        return words.toList()
    }
}

