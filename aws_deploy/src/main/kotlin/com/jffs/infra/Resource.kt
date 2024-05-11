package com.jffs.infra

interface Resource {
    fun name() : String

    fun create()

    fun checkIfExists() : Boolean

    fun delete()

    fun dependencies() : List<Resource> = listOf()
}