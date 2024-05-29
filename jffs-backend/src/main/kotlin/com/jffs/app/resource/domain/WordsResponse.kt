package com.jffs.app.resource.domain

import kotlinx.serialization.Serializable

@Serializable
data class WordsDTO(val words: List<WordDTO>)

@Serializable
data class WordDTO(
    val word: String,
    val meanings: List<MeaningDTO>
)

@Serializable
data class MeaningDTO(val definition: String,
                      val synonyms: List<String>? = listOf(),
                      val examples: List<String>? = listOf())


