package com.gt.functional.todolist

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError

interface ScenarioActor {
    val name: String
}

class TodoOwner(override val name: String) : ScenarioActor {
    fun canSeeList(listName: String, items: List<String>) : Step = {
        val expectedList = createList(listName, items)
        val list = getTodoList(name, listName)

        assertThat(list?.listName?.name).isEqualTo(listName)
        assertThat(list).isEqualTo(expectedList)
    }

    fun cannotSeeList(listName: String): Step = {
        assertThrows<AssertionFailedError> { getTodoList(name, listName) }
    }

    companion object {
        fun createList(listName: String, items: List<String>) = TodoList(ListName(listName), items.map(::TodoItem))
    }
}
