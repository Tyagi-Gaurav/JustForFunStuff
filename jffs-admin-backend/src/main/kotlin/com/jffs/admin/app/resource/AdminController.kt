package com.jffs.admin.app.resource

import com.jffs.admin.app.db.VocabRepository
import com.jffs.admin.app.domain.Meaning
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class AdminController(@Autowired val vocabRepository: VocabRepository) {
    @GetMapping("/v1/words/{pageNum}", produces = ["application/json"])
    suspend fun getPaginatedWords(@PathVariable("pageNum") pageNum: String): ResponseEntity<PageinatedWordsDTO> {
        val paginatedWords = vocabRepository.readAllWords(Integer.parseInt(pageNum))
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
            }
        return ResponseEntity.ok(
            PageinatedWordsDTO(words,
                paginatedWords.totalPages,
                paginatedWords.currentPage,
                paginatedWords.nextPage,
                paginatedWords.previousPage,
                ));
    }
}