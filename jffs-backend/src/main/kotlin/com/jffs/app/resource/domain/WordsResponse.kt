package com.jffs.app.resource.domain

import kotlinx.serialization.Serializable

@Serializable
data class WordsDTO(val words: List<WordDTO>)

@Serializable
data class WordDTO(
    val id: Int,
    val word: String,
    val meaning: List<String>,
    val synonyms: List<String>? = listOf(),
    val examples: List<String>? = listOf()
)


