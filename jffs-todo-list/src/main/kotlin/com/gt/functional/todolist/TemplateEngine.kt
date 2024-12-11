package com.gt.functional.todolist

data class StringTag(val text: String)

infix fun String.tag(value: String): Pair<String, StringTag> = Pair(this, StringTag(value))

object TemplateEngine {
    fun renderTemplate(template: String, data: Map<String, StringTag>) : String =
        //Accumulates the result
        data.entries.fold(template) {
            acc, (key, value) -> acc.replace("{$key}", value.text)
        }

}