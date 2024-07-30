package com.jffs.tests

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jffs.app.JffsApplication
import com.jffs.app.domain.Meaning
import com.jffs.app.domain.Word
import com.jffs.app.resource.domain.UIEvent
import com.jffs.tests.initializer.TestContainerDatabaseInitializer
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import java.net.URI
import java.time.LocalDateTime
import java.time.ZoneOffset

@ContextConfiguration(initializers = [TestContainerDatabaseInitializer::class])
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JffsApplication::class])
@AutoConfigureMockMvc
class ControllersTest {
    @Autowired
    val mongoClient: MongoClient? = null;
    var collection: MongoCollection<Word>? = null
    var modifiedTime = LocalDateTime.now(ZoneOffset.UTC)

    @Autowired
    lateinit var wac: WebApplicationContext

    lateinit var client: WebTestClient

    @Autowired
    lateinit var mockMvc: MockMvc

    private val objectMapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        client = MockMvcWebTestClient.bindToApplicationContext(wac).build()
        val database = mongoClient?.getDatabase("testDB")
        collection = database?.getCollection<Word>("word")
        runBlocking {
            collection?.insertMany(
                listOf(
                    Word(
                        "A Word 1",
                        listOf(
                            Meaning(
                                "A definition 1",
                                listOf("synonym1", "synonym2"),
                                listOf("example1", "example2")
                            )
                        ),
                        modifiedTime
                    ),
                    Word(
                        "A Word 2",
                        listOf(
                            Meaning(
                                "A definition 2",
                                listOf("synonym1", "synonym2"),
                                listOf("example1", "example2")
                            )
                        ),
                        null
                    )
                )
            )
        }
    }

    @Test
    fun getWords() {
        client.get().uri("/v1/words")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType("application/json")
            .expectBody()
            .jsonPath("$.words[*].word").value(containsInAnyOrder("A Word 1", "A Word 2"))
            .jsonPath("$.words[*].meanings[*].definition").value(containsInAnyOrder("A definition 1", "A definition 2"))

            .jsonPath("$.words[0].meanings[0].synonyms[0]").isEqualTo("synonym1")
            .jsonPath("$.words[0].meanings[0].synonyms[1]").isEqualTo("synonym2")
            .jsonPath("$.words[0].meanings[0].examples.length()").isEqualTo(2)
            .jsonPath("$.words[0].meanings[0].examples[0]").isEqualTo("example1")
            .jsonPath("$.words[0].meanings[0].examples[1]").isEqualTo("example2")

            .jsonPath("$.words[1].meanings[0].synonyms[0]").isEqualTo("synonym1")
            .jsonPath("$.words[1].meanings[0].synonyms[1]").isEqualTo("synonym2")
            .jsonPath("$.words[1].meanings[0].examples.length()").isEqualTo(2)
            .jsonPath("$.words[1].meanings[0].examples[0]").isEqualTo("example1")
            .jsonPath("$.words[1].meanings[0].examples[1]").isEqualTo("example2")
    }

    @Test
    fun postEvent() {
        val uiEvent = UIEvent(
            "testPage",
            "testComponent",
            "testAction"
        )
        mockMvc.perform(
            post(URI("/v1/ui/event"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(uiEvent))
        ).andExpect(status().isOk)
    }
}