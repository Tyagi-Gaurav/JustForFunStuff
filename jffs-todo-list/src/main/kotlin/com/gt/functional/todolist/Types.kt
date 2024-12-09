package com.gt.functional.todolist

data class TodoList(val listName: ListName, val items : List<TodoItem>)

data class TodoItem(val description : String)
enum class TodoStatus {Todo, InProgress, Done, Blocked}

data class ListName(val name : String)

data class User(val name: String)
data class Html(val raw : String)