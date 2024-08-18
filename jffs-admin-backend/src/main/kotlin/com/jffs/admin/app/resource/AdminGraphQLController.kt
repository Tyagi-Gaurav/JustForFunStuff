package com.jffs.admin.app.resource

import com.jffs.admin.app.db.AdminRepository
import com.jffs.admin.app.domain.Meaning
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

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
}