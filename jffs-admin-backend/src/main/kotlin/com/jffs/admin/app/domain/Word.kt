package com.jffs.admin.app.domain

data class Word(
    val word: String,
    val meanings: List<Meaning>
)

data class Meaning(
    val definition: String,
    val synonyms: List<String>? = listOf(),
    val examples: List<String>? = listOf()
)