package com.jffs.app.domain

data class Word(
    val word: String,
    val meaning: List<String>,
    val synonyms: List<String>? = listOf(),
    val examples: List<String>? = listOf()
)