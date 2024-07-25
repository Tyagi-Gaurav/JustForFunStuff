package com.jffs.app.resource

import com.jffs.app.db.VocabRepository
import com.jffs.app.domain.Meaning
import com.jffs.app.resource.domain.MeaningDTO
import com.jffs.app.resource.domain.WordDTO
import com.jffs.app.resource.domain.WordsDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class VocabController(@Autowired val vocabRepository: VocabRepository) {
    @GetMapping("/v1/words", produces = ["application/json"])
    suspend fun getWords(): ResponseEntity<WordsDTO> {
        val wordsDTO = WordsDTO(
            vocabRepository.readAllWords()
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
                })
        return ResponseEntity.ok(wordsDTO);
    }
}