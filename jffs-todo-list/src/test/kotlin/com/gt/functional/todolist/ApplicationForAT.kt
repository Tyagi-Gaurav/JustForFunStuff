package com.gt.functional.todolist

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Assertions.fail

/*
    Step is a type of function that takes an Action as a Receiver and returns Unit

 */
typealias Step = Actions.() -> Unit

class ApplicationForAT(
    val client: HttpHandler,
    val server: AutoCloseable
) : Actions {

    /*
    Each step uses side effects to modify the system and they'll raise an errror rather
    than returning a result.
     */
    fun runScenario(vararg  steps: Step) {
        /*
        .use() means that server would be closed as soon as the scope ends.
         */
        server.use { steps.onEach { step -> step(this) } }
    }

    override fun getTodoList(user: String, listName: String): TodoList? {
        val request = Request(
            Method.GET,
            "http://localhost:8081/todo/$user/$listName"
        )

        val response = client(request)

        return if (response.status == Status.OK)
            parseResponse(response.bodyString())
        else
            fail(response.toMessage())
    }

    private fun parseResponse(html: String): TodoList {
        val nameRegex = "<h2>.*<".toRegex()
        val listName = ListName(extractListName(nameRegex, html))
        val itemsRegex = "<td>.*?<".toRegex()
        val items = itemsRegex.findAll(html)
            .map { TodoItem(extractItemDesc(it)) }.toList()
        return TodoList(listName, items)
    }

    private fun extractListName(nameRegex: Regex, html: String): String =
        nameRegex.find(html)?.value
            ?.substringAfter("<h2>")
            ?.dropLast(1)
            .orEmpty()

    private fun extractItemDesc(matchResult: MatchResult): String =
        matchResult.value.substringAfter("<td>").dropLast(1)
}