package com.jffs.infra

class Vpc : Resource {
    override fun name() : String {
        return "Vpc"
    }

    override fun create() {
        println ("Created Vpc");
    }

    override fun checkIfExists(): Boolean = false

    override fun delete() {
        TODO("Not yet implemented")
    }
}