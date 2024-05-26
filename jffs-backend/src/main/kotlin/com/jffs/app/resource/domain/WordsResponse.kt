package com.jffs.app

import kotlinx.serialization.Serializable

@Serializable
data class WordsResponse(val words: Array<WordResponse>)

@Serializable
data class WordResponse(
    val id: Int,
    val word: String,
    val meaning: Array<String>,
    val synonyms: Array<String> = arrayOf(),
    val examples: Array<String> = arrayOf()
)


