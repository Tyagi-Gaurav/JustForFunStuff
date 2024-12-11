package com.gt.functional.todolist

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TemplateEngineTest {
    val template = """
 	    Happy Birthday {name} {surname}!
 	    from {sender}.
 	""".trimIndent()

    @Test
    fun templateEngine() {
        val data = mapOf(
            "name" tag "Jumbo",
            "surname" tag "Mumbo",
            "sender" tag "Mogambo")

        val actual = TemplateEngine.renderTemplate(template, data)

        val expected = """
 	    Happy Birthday Jumbo Mumbo!
 	    from Mogambo.
 	    """.trimIndent()

        assertThat(actual).isEqualTo(expected)

    }
}