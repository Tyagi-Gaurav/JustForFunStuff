package com.jffs.admin.app.tests

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jffs.admin.app.config.DatabaseConfig
import com.jffs.admin.app.db.AdminRepository
import com.jffs.admin.app.domain.Word
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors


class UploadWords {
    var totalAddedWords = 0
    val mapper = jacksonObjectMapper()

    companion object {
        val connectionString =
            "mongodb://root:example@localhost/?retryWrites=true&appName=testDB"
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .serverApi(serverApi)
            .build()
        val client = MongoClient.Factory.create(mongoClientSettings);
    }

    val adminRepository = AdminRepository(client, DatabaseConfig("", "", "", "vocab", "", ""))
    private suspend fun inDB(word: String): Boolean {
        return Optional.ofNullable(adminRepository.findByWord(word.trim()))
            .map { true }.orElse(false)
    }

    suspend fun upload() {
        val file = File("/Users/gauravtyagi/Downloads/gre_words_only.txt")
        val readLines = file.readLines()

        println("Total ReadLines: ${readLines.size}")

        for ((index, word) in readLines.withIndex()) {
            println("Checking word $word at index: $index")
            if (!inDB(word)) {
                val response = lookup(word)
                if (response.second) {
                    val wordResponse: List<Response> = mapper.readValue(response.first.toString())

                    val definitions = wordResponse.stream()
                        .flatMap { it.meanings.stream() }
                        .flatMap { it.definitions.stream() }
                        .map { it.definition }
                        .limit(3)
                        .collect(Collectors.joining(";"))

                    val synonyms = wordResponse.stream()
                        .flatMap { it.meanings.stream() }
                        .flatMap { it.definitions.stream() }
                        .flatMap { it.synonyms.stream() }
                        .limit(6)
                        .collect(Collectors.toList())

                    val example = wordResponse.stream()
                        .flatMap { it.meanings.stream() }
                        .flatMap { it.definitions.stream() }
                        .map { it?.example ?: "" }
                        .filter { it.isNotEmpty() }
                        .limit(3)
                        .collect(Collectors.toList())

                    insert(
                        Word(
                            word,
                            listOf(com.jffs.admin.app.domain.Meaning(definitions, synonyms, example)),
                            LocalDateTime.now()
                        ))
                    totalAddedWords++
                    print("Added $word\n")
                }
            } else {
                println("$word already exists in DB")
            }
        }

        print("Total words added: $totalAddedWords")
    }

    private suspend fun insert(word: Word) {
        adminRepository.add(word)
    }
}

suspend fun main() {
    UploadWords().upload()
}

fun lookup(word: String?): Pair<String?, Boolean> {
//    println("Looking up -$word-")
    val client = HttpClient.newBuilder().build();
    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.dictionaryapi.dev/api/v2/entries/en/${word!!.trim()}"))
        .build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    if (response.statusCode() != 200) {
        return Pair("", false)
    }
    return Pair(response.body(), true)
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Response(val word: String, val meanings: List<Meaning>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Meaning(val partOfSpeech: String, val definitions: List<Definition>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Definition(val definition: String, val example: String?, val synonyms: List<String>)


