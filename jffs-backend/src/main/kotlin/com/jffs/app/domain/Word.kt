package com.jffs.app.domain

data class Word(
    val id: Int,
    val word: String,
    val meaning: Array<String>,
    val synonyms: Array<String> = arrayOf(),
    val examples: Array<String> = arrayOf()
)