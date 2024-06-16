package com.jffs.admin.app.resource

import com.jffs.admin.app.db.AdminRepository
import com.jffs.admin.app.domain.Meaning
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class AdminController(@Autowired val adminRepository: AdminRepository) {
    @GetMapping("/v1/words/{pageNum}", produces = ["application/json"])
    suspend fun getPaginatedWords(@PathVariable("pageNum") pageNum: String): ResponseEntity<PaginatedWordsDTO> {
        val paginatedWords = adminRepository.readAllWords(Integer.parseInt(pageNum))
        val words = paginatedWords.words
            .map { word ->
                WordDTO(
                    word.word,
                    word.meanings.map { meaning: Meaning ->
                        MeaningDTO(
                            meaning.definition,
                            meaning.synonyms,
                            meaning.examples
                        )
                    })
            }.toList()
        val response = PaginatedWordsDTO(words,
            paginatedWords.totalPages,
            paginatedWords.currentPage,
            paginatedWords.nextPage,
            paginatedWords.previousPage,
        )
        return ResponseEntity.ok(response)
    }
}