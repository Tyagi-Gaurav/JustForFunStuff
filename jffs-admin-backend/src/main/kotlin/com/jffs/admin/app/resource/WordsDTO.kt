package com.jffs.admin.app.resource

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedWordsDTO(
    val words: List<WordDTO>,
    val totalWords: Int,
    val totalPages: Int,
    val currentPage: Int,
    val nextPage: Int,
    val previousPage: Int
)

@Serializable
data class WordDTO(
    val word: String,
    val meanings: List<MeaningDTO>
) {
    fun meanings(): List<MeaningDTO> {
        return meanings
    }
}

@Serializable
data class MeaningDTO(
    val definition: String,
    val synonyms: List<String>? = listOf(),
    val examples: List<String>? = listOf()
) {
    fun definition(): String {
        return definition
    }

    fun synonyms(): List<String>? {
        return synonyms
    }

    fun examples(): List<String>? {
        return examples
    }
}


