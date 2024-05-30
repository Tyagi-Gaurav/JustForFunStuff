package com.jffs.tests

import com.jffs.app.JffsApplication
import com.jffs.app.domain.Meaning
import com.jffs.app.domain.Word
import com.jffs.tests.initializer.TestContainerDatabaseInitializer
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ContextConfiguration(initializers = [TestContainerDatabaseInitializer::class])
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JffsApplication::class])
class VocabResourceTest {
    @Autowired
    val applicationContext: ApplicationContext? = null

    @Autowired
    val mongoClient: MongoClient? = null;
    var collection: MongoCollection<Word>? = null

    @BeforeEach
    fun setUp() {
        val database = mongoClient?.getDatabase("testDB")
        collection = database?.getCollection<Word>("word")
        runBlocking {
            collection?.insertOne(
                Word(
                    "A Word",
                    listOf(Meaning("A definition", listOf("synonym1", "synonym2"), listOf("example1", "example2")))
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
        result?.shouldHaveSize(1)
        result?.get(0) shouldBe Word(
            "A Word",
            listOf(Meaning("A definition", listOf("synonym1", "synonym2"), listOf("example1", "example2")))
        )
    }
}