package com.jffs.admin.app.resource

import com.jffs.admin.app.db.AdminRepository
import com.jffs.admin.app.domain.Meaning
import com.jffs.admin.app.domain.Word
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDateTime

@Controller
class AdminGraphQLController(@Autowired val adminRepository: AdminRepository) {
    private val LOG: Logger = LogManager.getLogger("APP")

    @QueryMapping
    suspend fun allWords(@Argument pageNum: Int): PaginatedWordsDTO {
        val paginatedWords = adminRepository.readAllWords(pageNum)
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
        return PaginatedWordsDTO(
            words,
            paginatedWords.totalWords,
            paginatedWords.totalPages,
            paginatedWords.currentPage,
            paginatedWords.nextPage,
            paginatedWords.previousPage,
        )
    }

    @QueryMapping
    suspend fun findWord(@Argument wordInput: String): WordDTO? {
        LOG.info("Request received for findWord for word ****$wordInput***")
        val databaseResponse = adminRepository.findByWord(wordInput)
        LOG.info("Database Response $databaseResponse")
        return databaseResponse?.let {
            WordDTO(
                it.word,
                it.meanings.map { meaning: Meaning ->
                    MeaningDTO(
                        meaning.definition,
                        meaning.synonyms,
                        meaning.examples
                    )
                })
        }
    }

    @MutationMapping
    suspend fun addWord(@Argument wordInput: WordInput): Boolean {
        LOG.info("Request received for addWord with $wordInput")
        val databaseResponse = adminRepository.findByWord(wordInput.word)
        databaseResponse?.let {
            return false
        }
        val wordToAdd = wordInput.let {
            Word(
                it.word.lowercase(),
                it.meanings.map { meaning: MeaningInput ->
                    Meaning(
                        meaning.definition,
                        meaning.synonyms?.map { it.trim() },
                        meaning.examples
                    )
                },
                LocalDateTime.now()
            )
        }
        adminRepository.add(wordToAdd)
        return true
    }

    @QueryMapping
    suspend fun search(
        @Argument searchType: SearchTypeInput,
        @Argument searchValue: String
    ): WordDTO? {
        LOG.info("Request received for search for searchType: $searchType, searchValue: $searchValue")
        val databaseResponse = if (searchType == SearchTypeInput.WORD) adminRepository.findByWord(searchValue) else
            adminRepository.findBySynonym(searchValue)

        return databaseResponse?.let {
            WordDTO(
                it.word,
                it.meanings.map { meaning: Meaning ->
                    MeaningDTO(
                        meaning.definition,
                        meaning.synonyms,
                        meaning.examples
                    )
                })
        }
    }

    @MutationMapping
    suspend fun updateWord(
        @Argument oldWord: String,
        @Argument wordInput: WordInput
    ): Boolean {
        LOG.info("Request received for Update word for word $oldWord with $wordInput")
        val word = wordInput.let {
            Word(
                it.word.lowercase(),
                it.meanings.map { meaning: MeaningInput ->
                    Meaning(
                        meaning.definition,
                        meaning.synonyms?.map { it.trim() },
                        meaning.examples
                    )
                },
                LocalDateTime.now()
            )
        }
        adminRepository.update(oldWord.lowercase(), word)
        return true
    }

    @MutationMapping
    suspend fun deleteWord(@Argument word: String): Boolean {
        LOG.info("Request received for deleteWord with $word")
        adminRepository.delete(word.lowercase())
        return true
    }
}