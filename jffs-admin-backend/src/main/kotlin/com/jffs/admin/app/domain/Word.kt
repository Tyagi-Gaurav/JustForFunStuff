package com.jffs.admin.app.domain

import java.time.LocalDateTime

data class Word(
    val word: String,
    val meanings: List<Meaning>,
    val modifiedDateTime: LocalDateTime?
)

data class Meaning(
    val definition: String,
    val synonyms: List<String>? = listOf(),
    val examples: List<String>? = listOf()
)

data class PaginatedWords(
    val words : List<Word>,
    val totalWords : Int,
    val totalPages : Int,
    val nextPage: Int,
    val previousPage: Int,
    val currentPage: Int
)