package com.jffs.admin.app.resource

import com.jffs.admin.app.db.AdminRepository
import com.jffs.admin.app.domain.Meaning
import com.jffs.admin.app.domain.Word
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDateTime

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
        val response = PaginatedWordsDTO(
            words,
            paginatedWords.totalPages,
            paginatedWords.currentPage,
            paginatedWords.nextPage,
            paginatedWords.previousPage,
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/v1/word/{word}", produces = ["application/json"])
    suspend fun findWord(@PathVariable("word") word: String): ResponseEntity<WordDTO> {
        val findByWord = adminRepository.findByWord(word)
        return findByWord?.let {
            ResponseEntity.ok(WordDTO(
                it.word,
                it.meanings.map { meaning: Meaning ->
                    MeaningDTO(
                        meaning.definition,
                        meaning.synonyms,
                        meaning.examples
                    )
                }))} ?: ResponseEntity.notFound().build()
    }

    @PostMapping("/v1/word/{word}", consumes = ["application/json"])
    suspend fun saveWord(@PathVariable("word") oldWord: String, @RequestBody wordDTO: WordDTO) : ResponseEntity<String> {
        val word = wordDTO.let {
            Word(
                it.word,
                it.meanings.map { meaning: MeaningDTO ->
                    Meaning(
                        meaning.definition,
                        meaning.synonyms,
                        meaning.examples
                    )
                },
                LocalDateTime.now()
            )
        }
        adminRepository.save(oldWord, word)
        return ResponseEntity.noContent().build()
    }
}