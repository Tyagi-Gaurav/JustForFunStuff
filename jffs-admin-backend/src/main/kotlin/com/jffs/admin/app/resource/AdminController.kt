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
    suspend fun getPage1Words(@PathVariable("pageNum") pageNum : String): ResponseEntity<WordsDTO> {
        val wordsDTO = WordsDTO(
            vocabRepository.readAllWords(Integer.parseInt(pageNum))
                .map { word ->
                    WordDTO(
                        word.word,
                        word.meanings.map { meaning: Meaning ->
                            MeaningDTO(
                                meaning.definition,
                                meaning.synonyms,
                                meaning.examples
                            )
                        },
                        word.paginationToken)
                })
        return ResponseEntity.ok(wordsDTO);
    }
}