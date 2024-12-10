package com.gt.functional.todolist


class FunStack<T>(private val list: List<T> = listOf()) {

    fun push(item: T): FunStack<T> {
        val newList = list.toMutableList()
        newList.add(item)
        return FunStack(newList.toList())
    }

    fun size(): Int = list.size

    fun pop(): Pair<FunStack<T>, T> {
        val newList = list.toMutableList()
        val item = newList.removeLast()
        return Pair(FunStack(newList.toList()), item)
    }
}
