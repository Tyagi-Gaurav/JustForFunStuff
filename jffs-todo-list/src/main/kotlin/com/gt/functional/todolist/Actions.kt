package com.gt.functional.todolist

interface Actions {
    fun getTodoList(user : String, listName: String) : TodoList?
}