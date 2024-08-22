package com.jffs.admin.app.tests

import com.jffs.admin.app.JffsAdminApplication
import com.jffs.admin.app.domain.Meaning
import com.jffs.admin.app.domain.Word
import com.jffs.admin.app.resource.WordDTO
import com.jffs.admin.app.tests.assertion.WordDTOAssert.assertThat
import com.jffs.admin.app.tests.initializer.TestContainerDatabaseInitializer
import com.mongodb.client.model.Filters.regex
import com.mongodb.client.model.IndexOptions
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import org.bson.BsonDocument
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.HttpGraphQlTester
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
@AutoConfigureHttpGraphQlTester
class AdminControllerTest {
    @Autowired
    val mongoClient: MongoClient? = null
    var collection: MongoCollection<Word>? = null
    var modifiedTime = LocalDateTime.now(ZoneOffset.UTC)

    @Autowired
    lateinit var wac: WebApplicationContext;

    lateinit var tester: HttpGraphQlTester

    @BeforeEach
    fun setUp() {
        var client = MockMvcWebTestClient.bindToApplicationContext(wac)
            .configureClient()
            .baseUrl("/graphql")
            .build()

        tester = HttpGraphQlTester.create(client)

        val database = mongoClient?.getDatabase("testDB")
        collection = database?.getCollection<Word>("word")

        runBlocking {
            collection?.insertMany(generateListOfWords(25))
            collection?.find()?.count() shouldBe 25
            collection?.createIndex(
                BsonDocument.parse("{\"word\": 1}"),
                IndexOptions()
                    .unique(true)
                    .background(false)
            )
        }
    }

    @Test
    fun getPaginatedWords() {
        val requestTemplate = """
            {
                allWords(pageNum: %d) {
                    words {
                        word
                        meanings {
                            definition
                            synonyms
                            examples
                        }
                    }
                    totalWords
                    totalPages
                    currentPage
                    nextPage
                    previousPage
                }
            }
        """
        var response = tester.document(requestTemplate.trimIndent().format(1)).execute()
        response
            .path("data.allWords.words").entityList(WordDTO::class.java).hasSize(10)
            .path("data.allWords.totalWords").entity(Int::class.java).isEqualTo(25)
            .path("data.allWords.totalPages").entity(Int::class.java).isEqualTo(3)
            .path("data.allWords.currentPage").entity(Int::class.java).isEqualTo(1)
            .path("data.allWords.nextPage").entity(Int::class.java).isEqualTo(2)
            .path("data.allWords.previousPage").entity(Int::class.java).isEqualTo(-1)

        response = tester.document(requestTemplate.trimIndent().format(2)).execute()
        response
            .path("data.allWords.words").entityList(WordDTO::class.java).hasSize(10)
            .path("data.allWords.totalWords").entity(Int::class.java).isEqualTo(25)
            .path("data.allWords.totalPages").entity(Int::class.java).isEqualTo(3)
            .path("data.allWords.currentPage").entity(Int::class.java).isEqualTo(2)
            .path("data.allWords.nextPage").entity(Int::class.java).isEqualTo(3)
            .path("data.allWords.previousPage").entity(Int::class.java).isEqualTo(1)

        response = tester.document(requestTemplate.trimIndent().format(3)).execute()
        response
            .path("data.allWords.words").entityList(WordDTO::class.java).hasSize(5)
            .path("data.allWords.totalWords").entity(Int::class.java).isEqualTo(25)
            .path("data.allWords.totalPages").entity(Int::class.java).isEqualTo(3)
            .path("data.allWords.currentPage").entity(Int::class.java).isEqualTo(3)
            .path("data.allWords.nextPage").entity(Int::class.java).isEqualTo(-1)
            .path("data.allWords.previousPage").entity(Int::class.java).isEqualTo(2)
    }

    @Test
    fun addWordThatDoesNotExist() {
        assertThat(findWord("ANewWord1")).isNull();
        val requestTemplate = """
            mutation {
                addWord(wordInput: {
                    word: "ANewWord1",
                    meanings: [{
                        definition: "A new definition 1",
                        synonyms: ["new synonym1"],
                        examples: ["new example1"]
                    }]
                })}
                """

        val response = tester.document(requestTemplate.trimIndent()).execute()
        response.path("data.addWord").entity(Boolean::class.java).isEqualTo(true)
        assertThat(findWord("ANewWord1"))
            .isWord("anewword1")
            .hasDefinition("A new definition 1")
            .containsSynonyms("new synonym1")
            .containsExamples("new example1")
    }

    @Test
    fun addWordThatDoesAlreadyExists() {
        assertThat(findWord("AWord19")).isWord("aword19")

        val requestTemplate = """
            mutation {
                addWord(wordInput: {
                    word: "AWord19",
                    meanings: [{
                        definition: "A new definition 19",
                        synonyms: ["new synonym19"],
                        examples: ["new example19"]
                    }]
                })}
                """

        val response = tester.document(requestTemplate.trimIndent()).execute()
        response.path("data.addWord").entity(Boolean::class.java).isEqualTo(false)
    }

    @Test
    fun delete() {
        assertThat(findWord("AWord25")).isWord("aword25")
        val requestTemplate = """
            mutation {
                deleteWord(word: "AWord25")
            }"""

        tester.document(requestTemplate.trimIndent()).execute()
        assertThat(findWord("AWord25")).isNull()
    }

    @Test
    fun update() {
        assertThat(findWord("AWord19"))
            .isWord("aword19")
            .hasDefinition("A definition 19")
            .containsSynonyms("synonym19")
            .containsExamples("example19")

        val requestTemplate = """
            mutation {
                updateWord(oldWord: "AWord19", wordInput: {
                    word: "AWord19"
                    meanings: [{
                        definition: "A new definition 19",
                        synonyms: ["new synonym19"],
                        examples: ["new example19"]
                    }]
                })
            }
        """.trimIndent()
        tester.document(requestTemplate.trimIndent()).execute()

        assertThat(findWord("AWord19"))
            .isWord("aword19")
            .hasDefinition("A new definition 19")
            .containsSynonyms("new synonym19")
            .containsExamples("new example19")
    }

    @ParameterizedTest
    @ValueSource(strings = ["aword19", "AWORD19", "AWord19"])
    fun searchWordShouldBeCaseInsensitive(word: String) {
        val requestTemplate = """
            {
                search(searchType : WORD, searchValue: "$word") {
                    word
                    meanings {
                        synonyms
                        examples
                    }
                }
            }
        """.trimIndent()
        val response = tester.document(requestTemplate.trimIndent()).execute()

        response
            .path("data.search.word").entity(String::class.java).isEqualTo(word.lowercase())
            .path("data.search.meanings[0].synonyms[0]").entity(String::class.java).isEqualTo("synonym19")
            .path("data.search.meanings[0].examples[0]").entity(String::class.java).isEqualTo("example19")
    }

    @Test
    fun findWords() {
        assertThat(findWord("aword20"))
            .isWord("aword20")
            .hasDefinition("A definition 20")
            .containsSynonyms("synonym20")
            .containsExamples("example20")
    }

    @Test
    fun searchSynonym() {
        val requestTemplate = """
            {
                search(searchType : SYNONYM, searchValue: "synonym1") {
                    word
                    meanings {
                        definition
                        synonyms
                        examples
                    }
                }
            }
        """.trimIndent()
        val response = tester.document(requestTemplate.trimIndent()).execute()

        response
            .path("data.search.word").entity(String::class.java).isEqualTo("aword1")
            .path("data.search.meanings[0].definition").entity(String::class.java).isEqualTo("A definition 1")
            .path("data.search.meanings[0].synonyms[0]").entity(String::class.java).isEqualTo("synonym1")
            .path("data.search.meanings[0].examples[0]").entity(String::class.java).isEqualTo("example1")
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "WORD, abb",
            "SYNONYM, ajhdjd"
        ]
    )
    fun searchUnAvailableStringsOrTypes(searchType: String, searchValue: String) {
        val requestTemplate = """
            {
                search(searchType : $searchType, searchValue: "$searchValue") {
                    word
                }
            }
        """.trimIndent()
        val response = tester.document(requestTemplate.trimIndent()).execute()

        response.path("data.search.word").pathDoesNotExist()
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
                    "aword$index",
                    listOf(Meaning("A definition $index", listOf("synonym$index"), listOf("example$index"))),
                    modifiedTime
                )
            )
        }
        return words.toList()
    }

    private fun findWord(wordToFind: String): WordDTO? {
        val requestTemplate = """
            {
                findWord(wordInput: "$wordToFind") {
                    word
                    meanings {
                        definition
                        synonyms
                        examples
                    }
                }
            }
        """.trimIndent()

        val path = tester.document(requestTemplate.trimIndent())
            .execute()
            .path("data.findWord")

        return try {
            path.entity(WordDTO::class.java).get()
        } catch (e: Exception) {
            null
        }
    }
}
