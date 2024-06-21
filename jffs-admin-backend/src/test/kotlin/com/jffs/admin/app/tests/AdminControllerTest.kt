package com.jffs.admin.app.tests

import com.jffs.admin.app.JffsAdminApplication
import com.jffs.admin.app.domain.Meaning
import com.jffs.admin.app.domain.Word
import com.jffs.admin.app.resource.MeaningDTO
import com.jffs.admin.app.resource.WordDTO
import com.jffs.admin.app.tests.initializer.TestContainerDatabaseInitializer
import com.mongodb.client.model.Filters.regex
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.count
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.body
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
            collection?.insertMany(generateListOfWords(25))
            collection?.find()?.count() shouldBe 25
        }
    }

    @Test
    fun getPaginatedWords() {
        client.get().uri("/v1/words/page/1")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.words.length()").isEqualTo(10)
            .jsonPath("$.totalPages").isEqualTo("3")
            .jsonPath("$.currentPage").isEqualTo("1")
            .jsonPath("$.nextPage").isEqualTo("2")
            .jsonPath("$.previousPage").isEqualTo("-1")

        client.get().uri("/v1/words/page/2")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.words.length()").isEqualTo(10)
            .jsonPath("$.totalPages").isEqualTo("3")
            .jsonPath("$.currentPage").isEqualTo("2")
            .jsonPath("$.nextPage").isEqualTo("3")
            .jsonPath("$.previousPage").isEqualTo("1")

        client.get().uri("/v1/words/page/3")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.words.length()").isEqualTo(5)
            .jsonPath("$.totalPages").isEqualTo("3")
            .jsonPath("$.currentPage").isEqualTo("3")
            .jsonPath("$.nextPage").isEqualTo("-1")
            .jsonPath("$.previousPage").isEqualTo("2")
    }

    @Test
    fun addWordThatDoesNotExist() {
        client.get().uri("/v1/words/ANewWord1")
            .exchange()
            .expectStatus().isNotFound

        val newWord: Deferred<WordDTO> =
            GlobalScope.async {
                WordDTO(
                    "ANewWord1",
                    listOf(MeaningDTO("A new definition 1", listOf("new synonym1"), listOf("new example1")))
                )
            }
        client.post().uri("/v1/words")
            .body<WordDTO>(newWord)
            .header("Content-Type", "application/vnd+add.word.v1+json")
            .exchange()
            .expectStatus()
            .isNoContent

        client.get().uri("/v1/words/ANewWord1")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.word").isEqualTo("ANewWord1")
            .jsonPath("$.meanings[0].definition").isEqualTo("A new definition 1")
            .jsonPath("$.meanings[0].synonyms[0]").isEqualTo("new synonym1")
            .jsonPath("$.meanings[0].examples[0]").isEqualTo("new example1")
    }

    @Test
    fun addWordThatDoesAlreadyExists() {
        client.get().uri("/v1/words/AWord1")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.word").isEqualTo("AWord1")

        val newWord: Deferred<WordDTO> =
            GlobalScope.async {
                WordDTO(
                    "AWord1",
                    listOf(MeaningDTO("A new definition 1", listOf("new synonym1"), listOf("new example1")))
                )
            }
        client.post().uri("/v1/words")
            .body<WordDTO>(newWord)
            .header("Content-Type", "application/vnd+add.word.v1+json")
            .exchange()
            .expectStatus()
            .is4xxClientError
    }

    @Test
    fun update() {
        client.get().uri("/v1/words/AWord1")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.word").isEqualTo("AWord1")
            .jsonPath("$.meanings[0].definition").isEqualTo("A definition 1")
            .jsonPath("$.meanings[0].synonyms[0]").isEqualTo("synonym1")
            .jsonPath("$.meanings[0].examples[0]").isEqualTo("example1")

        val updatedWordDTO: Deferred<WordDTO> =
            GlobalScope.async {
                WordDTO(
                    "AWord1",
                    listOf(MeaningDTO("A new definition 1", listOf("new synonym1"), listOf("new example1")))
                )
            }
        client.put().uri("/v1/words/AWord1")
            .body<WordDTO>(updatedWordDTO)
            .header("Content-Type", "application/vnd+update.word.v1+json")
            .exchange()
            .expectStatus()
            .isNoContent

        client.get().uri("/v1/words/AWord1")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.word").isEqualTo("AWord1")
            .jsonPath("$.meanings[0].definition").isEqualTo("A new definition 1")
            .jsonPath("$.meanings[0].synonyms[0]").isEqualTo("new synonym1")
            .jsonPath("$.meanings[0].examples[0]").isEqualTo("new example1")
    }

    @Test
    fun delete() {
        client.get().uri("/v1/words/AWord25")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.word").isEqualTo("AWord25")

        client.delete().uri("/v1/words/AWord25")
            .exchange()
            .expectStatus()
            .isAccepted

        client.get().uri("/v1/words/AWord25")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun searchWord() {
        client.get().uri("/v1/words/search?searchType=WORD&searchValue=AWord1")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.word").isEqualTo("AWord1")
            .jsonPath("$.meanings[0].definition").isEqualTo("A definition 1")
            .jsonPath("$.meanings[0].synonyms[0]").isEqualTo("synonym1")
            .jsonPath("$.meanings[0].examples[0]").isEqualTo("example1")
    }

    @Test
    fun searchSynonym() {
        client.get().uri("/v1/words/search?searchType=SYNONYM&searchValue=synonym1")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.word").isEqualTo("AWord1")
            .jsonPath("$.meanings[0].definition").isEqualTo("A definition 1")
            .jsonPath("$.meanings[0].synonyms[0]").isEqualTo("synonym1")
            .jsonPath("$.meanings[0].examples[0]").isEqualTo("example1")
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "WORD, abb",
            "SYNONYM, ajhdjd",
            "abc, ajhdjd"
        ]
    )
    fun searchUnAvailableStringsOrTypes(searchType: String, searchValue: String) {
        client.get().uri("/v1/words/search?searchType=$searchType&searchValue=$searchValue")
            .exchange()
            .expectStatus().isNotFound
    }

    @AfterEach
    fun tearDown() {
        runBlocking {
            collection?.deleteMany(regex("word", ".*"))
            collection?.find()?.count() shouldBe 0
        }
    }

    private fun generateListOfWords(wordsCount: Int): List<Word> {
        val words = mutableListOf<Word>()
        for (index in 1..wordsCount) {
            words.add(
                Word(
                    "AWord$index",
                    listOf(Meaning("A definition $index", listOf("synonym$index"), listOf("example$index"))),
                    modifiedTime
                )
            )
        }
        return words.toList()
    }
}