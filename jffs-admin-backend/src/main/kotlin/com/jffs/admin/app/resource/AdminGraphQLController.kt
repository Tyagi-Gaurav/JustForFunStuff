package com.jffs.admin.app.resource

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class AdminGraphQLController {
    private val LOG: Logger = LogManager.getLogger("APP")

    @QueryMapping
    fun allWords(): PaginatedWordsDTO {
        return PaginatedWordsDTO(
            listOf(
                WordDTO(
                    "aWord",
                    listOf(MeaningDTO("aDefinition", listOf("Synonyms"), listOf("Examples")))
                )), 1, 1, 1, -1, -1)
    }
}