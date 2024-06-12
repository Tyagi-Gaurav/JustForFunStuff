package com.jffs.tests

import com.jffs.app.JffsApplication
import com.jffs.app.domain.Meaning
import com.jffs.app.domain.Word
import com.jffs.tests.initializer.TestContainerDatabaseInitializer
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.date.shouldBeWithin
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Duration.ofSeconds
import java.time.LocalDateTime
import java.time.ZoneOffset

@ContextConfiguration(initializers = [TestContainerDatabaseInitializer::class])
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JffsApplication::class])
class VocabResourceTest {
    @Autowired
    val mongoClient: MongoClient? = null;
    var collection: MongoCollection<Word>? = null
    var modifiedTime = LocalDateTime.now(ZoneOffset.UTC)

    @BeforeEach
    fun setUp() {
        val database = mongoClient?.getDatabase("testDB")
        collection = database?.getCollection<Word>("word")
        runBlocking {
            collection?.insertMany(
                listOf(
                    Word(
                        "A Word 1",
                        listOf(Meaning("A definition 1", listOf("synonym1", "synonym2"), listOf("example1", "example2"))),
                        modifiedTime
                    ),
                    Word(
                        "A Word 2",
                        listOf(Meaning("A definition 2", listOf("synonym1", "synonym2"), listOf("example1", "example2"))),
                        null
                    )
                )
            )
        }
    }

    @Test
    fun getWords() {
        val result = runBlocking {
            collection?.find()?.toList()
        }

        result shouldNotBe null
        result?.shouldHaveSize(2)
        result?.get(0)?.word shouldBe "A Word 1"
        result?.get(0)?.meanings shouldBe listOf(
            Meaning(
                "A definition 1",
                listOf("synonym1", "synonym2"),
                listOf("example1", "example2")
            )
        )
        result?.get(0)?.modifiedDateTimestamp?.shouldBeWithin(ofSeconds(1), modifiedTime)

        result?.get(1)?.word shouldBe "A Word 2"
        result?.get(1)?.meanings shouldBe listOf(
            Meaning(
                "A definition 2",
                listOf("synonym1", "synonym2"),
                listOf("example1", "example2")
            )
        )
        result?.get(1)?.modifiedDateTimestamp shouldBe null
    }
}