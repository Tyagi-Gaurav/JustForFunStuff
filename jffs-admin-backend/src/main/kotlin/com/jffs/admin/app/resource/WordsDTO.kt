package com.jffs.admin.app.resource

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedWordsDTO(
    val words: List<WordDTO>,
    val totalPages: Int,
    val currentPage: Int,
    val nextPage: Int,
    val previousPage : Int
)

@Serializable
data class WordDTO(
    val word: String,
    val meanings: List<MeaningDTO>
)

@Serializable
data class MeaningDTO(
    val definition: String,
    val synonyms: List<String>? = listOf(),
    val examples: List<String>? = listOf()
)


