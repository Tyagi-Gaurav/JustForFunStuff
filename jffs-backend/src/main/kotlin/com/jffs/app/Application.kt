package com.jffs.app

import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

val fileContent = Application::class.java.getResource("/words.json").readText()

fun Application.module() {
    routing {
        get("/api/v1/words") {
            val words = Json.decodeFromString<Words>(fileContent)
            call.respondText(Json.encodeToString(words))
        }
    }
}