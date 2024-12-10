package com.gt.functional.todolist

import org.assertj.core.api.Assertions.assertThat
import org.http4k.client.JettyClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError

class SeeATodoListTest {
    private var server : Http4kServer? = null

    @Test
    fun `List owners can see their lists`() {
        val user = "a-test-user"
        val listName = "a-test-list-name"
        val listItems = listOf("itemA", "itemB", "itemC")

        startTheApplication(user, listName, listItems)

        val list = getTodoList(user, listName)

        assertThat(list.listName.name).isEqualTo(listName)
        assertThat(list.items.map { it.description }).isEqualTo(listItems)
    }

    @Test
    fun `Only owners can see their lists`() {
        val listName = "someOtherList"

        startTheApplication("someUser", listName, emptyList())

        assertThrows<AssertionFailedError> { getTodoList("someOtherUser", "listName") }
    }

    @AfterEach
    fun stopApplication() {
        server!!.stop()
    }

    private fun getTodoList(user: String, listName: String): TodoList {
        val client = JettyClient()
        val request = Request(
            Method.GET,
            "http://localhost:8081/todo/$user/$listName")

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

    private fun extractListName(nameRegex: Regex, html: String) : String =
        nameRegex.find(html)?.value
            ?.substringAfter("<h2>")
            ?.dropLast(1)
            .orEmpty()

    private fun extractItemDesc(matchResult: MatchResult) : String =
        matchResult.value.substringAfter("<td>").dropLast(1)

    private fun startTheApplication(user: String, listName: String, listItems: List<String>) {
        val todoList = TodoList(ListName(listName), listItems.map(::TodoItem))
        val lists = mapOf(User(user) to listOf(todoList))
        server = Zettai(lists).asServer(Jetty(8081))
        server!!.start()
    }
}