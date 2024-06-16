package com.jffs.admin.app.tests

import com.jffs.admin.app.JffsAdminApplication
import com.jffs.admin.app.domain.Meaning
import com.jffs.admin.app.domain.Word
import com.jffs.admin.app.tests.initializer.TestContainerDatabaseInitializer
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDateTime
import java.time.ZoneOffset


@ContextConfiguration(initializers = [TestContainerDatabaseInitializer::class])
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JffsAdminApplication::class])
class AdminControllerTest {
    @Autowired
    val mongoClient: MongoClient? = null
    var collection: MongoCollection<Word>? = null
    var modifiedTime = LocalDateTime.now(ZoneOffset.UTC)

    @Autowired
    lateinit var wac: WebApplicationContext;

    lateinit var client: WebTestClient

    @BeforeEach
    fun setUp() {
        client = MockMvcWebTestClient.bindToApplicationContext(wac).build()
        val database = mongoClient?.getDatabase("testDB")
        collection = database?.getCollection<Word>("word")
        runBlocking {
            collection?.insertMany(
                generateListOfWords(25)
            )
            println("Total number of words : ${collection?.find()?.count()}")
        }
    }

    @Test
    fun getPaginatedWords() {
            client.get().uri("/v1/words/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.totalPages").isEqualTo("3")
                .jsonPath("$.currentPage").isEqualTo("1")
                .jsonPath("$.nextPage").isEqualTo("2")
                .jsonPath("$.previousPage").isEqualTo("-1")
    }

    private fun generateListOfWords(wordsCount: Int): List<Word> {
        val words = mutableListOf<Word>()
        for (index in 1..wordsCount) {
            words.add(
                Word(
                    "A Word $index",
                    listOf(Meaning("A definition $index", listOf("synonym$index"), listOf("example$index"))),
                    modifiedTime
                )
            )
        }
        return words.toList()
    }
}