package com.gt.functional.todolist.pure

object IsSorted {
    val <T> List<T>.tail: List<T>
        get() = drop(1)

    val <T> List<T>.head: T
        get() = first()

    fun <A> isSorted(aa: List<A>, order: (A, A) -> Boolean): Boolean {
        tailrec fun loop(element: A, list: List<A>): Boolean {
            return if (list.isEmpty()) {
                true
            } else {
                if (order(element, list.head)) {
                    loop(list.head, list.tail)
                } else {
                    false
                }
            }
        }

        return loop(aa.head, aa.tail)
    }
}

fun main() {
    println(IsSorted.isSorted(listOf(1, 2, 3, 4, 5)) { a, b -> a > b })
}