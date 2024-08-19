package com.jffs.admin.app.resource

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedWordsInput(
    val words: List<WordInput>,
    val totalWords: Int,
    val totalPages: Int,
    val currentPage: Int,
    val nextPage: Int,
    val previousPage: Int
)

@Serializable
data class WordInput(
    val word: String,
    val meanings: List<MeaningInput>
)

@Serializable
data class MeaningInput(
    val definition: String,
    val synonyms: List<String>? = listOf(),
    val examples: List<String>? = listOf()
)


