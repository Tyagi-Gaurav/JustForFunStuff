package com.gt.functional.todolist

import com.gt.functional.todolist.TodoOwner.Companion.createList
import org.http4k.client.JettyClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.junit.jupiter.api.Test

class SeeATodoListTest {
    fun TodoOwner.asUser() : User = User(name)

    @Test
    fun `List owners can see their lists`() {
        val listName = "a-test-list-name"
        val listItems = listOf("itemA", "itemB", "itemC")
        val owner = TodoOwner("a-test-user")

        val application = startTheApplication(
            mapOf(
                owner.asUser() to listOf(createList(listName, listItems))
            ))

        application.runScenario(owner.canSeeList(listName, listItems))
    }

    @Test
    fun `Only owners can see their lists`() {
        val owner = TodoOwner("a-test-user")
        val listName = "a-test-list-name"

        val application = startTheApplication(
            mapOf(
                owner.asUser() to listOf(createList("another-list", emptyList()))
            ))

        application.runScenario(owner.cannotSeeList(listName))
    }

    fun startTheApplication(lists : Map<User, List<TodoList>>) : ApplicationForAT {
        val port = 8081
        val server = Zettai(ToDoListHub(lists)).asServer(Jetty(8081))
        server.start()

        val client = ClientFilters
            .SetBaseUriFrom(Uri.of("http://localhost:$port/"))
            .then(JettyClient())

        return ApplicationForAT(client, server)
    }

}