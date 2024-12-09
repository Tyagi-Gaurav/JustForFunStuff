package com.gt.functional.todolist

import org.http4k.server.Jetty
import org.http4k.server.asServer


fun main() {
    val items = listOf("write chapter", "insert code", "draw diagrams")

    val todoList = TodoList(ListName("book"), items.map(::TodoItem))
    val lists = mapOf(User("Username") to listOf(todoList))
    Zettai(lists).asServer(Jetty(8080)).start()

    println ("Server started at http://localhost:8080/todo/Username/book")
}