package com.jffs.admin.app.resource

import com.jffs.admin.app.db.AdminRepository
import com.jffs.admin.app.domain.Meaning
import com.jffs.admin.app.domain.Word
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Controller
class AdminController(@Autowired val adminRepository: AdminRepository) {
    @GetMapping("/v1/words/page/{pageNum}", produces = ["application/json"])
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

    @GetMapping("/v1/words/{word}", produces = ["application/json"])
    suspend fun findWord(@PathVariable("word") word: String): ResponseEntity<WordDTO> {
        val databaseResponse = adminRepository.findByWord(word)
        return databaseResponse?.let {
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

    @GetMapping("/v1/words/search", produces = ["application/json"])
    suspend fun search(@RequestParam("searchType") searchType: String,
                       @RequestParam("searchValue") searchValue: String): ResponseEntity<WordDTO> {

        val databaseResponse = if (searchType == "WORD") adminRepository.findByWord(searchValue) else
            adminRepository.findBySynonym(searchValue)

        return databaseResponse?.let {
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

    @PutMapping("/v1/words/{word}", consumes = ["application/vnd+update.word.v1+json"])
    suspend fun updateWord(@PathVariable("word") oldWord: String, @RequestBody wordDTO: WordDTO) : ResponseEntity<String> {
        val word = wordDTO.let {
            Word(
                it.word,
                it.meanings.map { meaning: MeaningDTO ->
                    Meaning(
                        meaning.definition,
                        meaning.synonyms?.map { it.trim() },
                        meaning.examples
                    )
                },
                LocalDateTime.now()
            )
        }
        adminRepository.update(oldWord, word)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/v1/words", consumes = ["application/vnd+add.word.v1+json"])
    suspend fun addWord(@RequestBody wordDTO: WordDTO) : ResponseEntity<String> {
        val word = wordDTO.let {
            Word(
                it.word,
                it.meanings.map { meaning: MeaningDTO ->
                    Meaning(
                        meaning.definition,
                        meaning.synonyms?.map { it.trim() },
                        meaning.examples
                    )
                },
                LocalDateTime.now()
            )
        }
        adminRepository.add(word)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/v1/words/{word}")
    suspend fun deleteWord(@PathVariable("word") word: String) : ResponseEntity<String> {
        adminRepository.delete(word)
        return ResponseEntity.accepted().build()
    }
}