package com.jffs.app

import kotlinx.serialization.Serializable

@Serializable
data class Words(val words : Array<Word>)

@Serializable
data class Word(val id : Int, val word : String, val meaning: Array<String>, val synonyms: Array<String> = arrayOf())


