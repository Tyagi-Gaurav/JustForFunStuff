package com.jffs.app.resource.domain

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api")
class VocabController {
    @GetMapping("/v1/words", produces = ["application/json"])
    fun getWords(): String {
        val readText: String? = VocabController::class.java.getResource("/words.json")?.readText()
        return readText ?: ""
    }
}