package com.gt.functional.todolist

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes

typealias FUN<A, B> = (A) -> B

infix fun <A, B, C> FUN<A, B>.andThen(other: FUN<B, C>): FUN<A, C> {
    val left = this
    return object : FUN<A, C> {
        override fun invoke(p1: A): C =
            other.invoke(left.invoke(p1))
    }
}

class Zettai(val lists: Map<User, List<TodoList>>) : HttpHandler {
    val allRoutes = routes(
        "/todo/{user}/{list}" bind GET to ::fetchList,
    )

    val processFun = ::extractListData andThen
                     ::fetchListContent andThen
                     ::renderHtml andThen
                     ::createResponse

    fun fetchList(request: Request): Response = processFun(request)

    override fun invoke(request: Request): Response = allRoutes(request)

    fun extractListData(req: Request): Pair<User, ListName> {
        val user = req.path("user").orEmpty()
        val list = req.path("list").orEmpty()

        return User(user) to ListName(list)
    }

    fun fetchListContent(listId: Pair<User, ListName>): TodoList =
        lists[listId.first]
            ?.firstOrNull { it.listName == listId.second }
            ?: error("List unknown")

    fun renderHtml(todoList: TodoList): Html = Html(
        """
        <html>
        <body>
            <h1 style="text-align:center; font-size:3em;">Zettai</h1>
            <h2>${todoList.listName.name}</h2>
            <table>
                <tbody>${renderItems(todoList.items)}</tbody>
            </table>
        </body>
    </html>
    """.trimIndent()
    )

    private fun renderItems(items: List<TodoItem>) =
        items.joinToString("") {
            """
                <tr><td>${it.description}</td></tr>
                """.trimIndent().trimIndent()
        }

    fun createResponse(html: Html): Response = Response(Status.OK).body(html.raw)
}