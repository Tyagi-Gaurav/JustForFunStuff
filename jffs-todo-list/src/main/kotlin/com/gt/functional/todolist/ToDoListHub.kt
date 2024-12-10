package com.gt.functional.todolist

class ToDoListHub(val lists: Map<User, List<TodoList>>) : ZettaiHub {
    override fun getList(user: User, list: ListName): TodoList? =
        lists[user] ?. firstOrNull() {it.listName == list}
}