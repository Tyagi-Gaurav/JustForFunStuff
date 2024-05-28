package com.jffs.app.resource.domain

import kotlinx.serialization.json.Json
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class VocabController {
    @GetMapping("/v1/words", produces = ["application/json"])
    fun getWords(): ResponseEntity<WordsDTO> {
        val readText: String = VocabController::class.java.getResource("/words.json")?.readText()!!
        val response = Json.decodeFromString<WordsDTO>(readText)
        return ResponseEntity.ok(response);
    }
}