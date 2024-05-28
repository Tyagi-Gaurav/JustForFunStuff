package com.jffs.app.resource.domain

import kotlinx.serialization.Serializable

@Serializable
data class WordsDTO(val words: Array<WordDTO>)

@Serializable
data class WordDTO(
    val id: Int,
    val word: String,
    val meaning: Array<String>,
    val synonyms: Array<String> = arrayOf(),
    val examples: Array<String> = arrayOf()
)


