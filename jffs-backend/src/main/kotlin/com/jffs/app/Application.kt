package com.jffs.app

import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.*
import java.io.File

fun main(args: Array<String>): Unit = EngineMain.main(args)

val fileContent = Application::class.java.getResource("/words.json").readText()

fun Application.module() {
    routing {
        get("/") {
            try {
                Json.decodeFromString<Words>(fileContent)
                    .also {
                        println(it)
                    }
            } catch (e : Exception) {
                println(e.stackTraceToString())
            }
            call.respondText("Hello, world!")
        }
    }
}

fun readFileAsTextUsingInputStream(fileName: String)
        = File(fileName).inputStream()