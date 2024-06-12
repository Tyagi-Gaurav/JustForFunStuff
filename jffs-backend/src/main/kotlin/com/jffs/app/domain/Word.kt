package com.jffs.app.domain

import java.time.LocalDateTime

data class Word(
    val word: String,
    val meanings: List<Meaning>,
    val modifiedDateTimestamp: LocalDateTime?
)

data class Meaning(
    val definition: String,
    val synonyms: List<String>? = listOf(),
    val examples: List<String>? = listOf()
)