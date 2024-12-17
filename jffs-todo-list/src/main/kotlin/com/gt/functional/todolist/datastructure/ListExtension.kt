package com.gt.functional.todolist.datastructure

import com.gt.functional.todolist.datastructure.ListExtension.drop
import com.gt.functional.todolist.datastructure.ListExtension.dropWhile
import com.gt.functional.todolist.datastructure.ListExtension.setHead
import com.gt.functional.todolist.datastructure.ListExtension.tail

object ListExtension {
    fun <A> tail(xs: List<A>): List<A> =
        if (xs.isEmpty()) xs
        else
            xs.drop(1)

    fun <A> setHead(xs: List<A>, x: A): List<A> = listOf(x).plus(xs)

    fun <A> drop(l: List<A>, n: Int): List<A> {
        fun loop(c: Int, xs: List<A>): List<A> {
            return if (c == n) {
                return xs
            } else if (c > l.size) {
                return xs
            } else {
                loop(c + 1, tail(xs))
            }
        }

        return loop(0, l)
    }

    fun <A> dropWhile(l: List<A>, f: (A) -> Boolean): List<A> {
        fun loop(xs: List<A>): List<A> {
            return if (xs.isEmpty())
                xs
            else if (f(xs.first()))
                loop(tail(xs))
            else
                xs
        }

        return loop(l)
    }
}

fun main() {
    println(tail(listOf(1, 2, 3, 4)))
    println(setHead(listOf(1, 2, 3, 4), 5))

    println(drop(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 3))
    println(drop(listOf(1, 2), 3))
    println(dropWhile(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), { a -> a < 8 }))
}