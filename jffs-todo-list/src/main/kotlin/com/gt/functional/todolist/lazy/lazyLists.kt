package com.gt.functional.todolist.lazy

import com.gt.functional.todolist.exceptions.None
import com.gt.functional.todolist.exceptions.Option
import com.gt.functional.todolist.exceptions.Some

sealed class Stream<out A>

data class Cons<out A>(
    val head: () -> A,
    val tail: () -> Stream<A>
) : Stream<A>()

data object Empty : Stream<Nothing>()

fun <A> Stream<A>.headOption(): Option<A> =
    when (this) {
        is Cons -> Some(head())
        is Empty -> None
    }

fun <A> cons(hd: () -> A, tl: () -> Stream<A>): Stream<A> {
    val head: A by lazy(hd)
    val tail: Stream<A> by lazy(tl)
    return Cons({ head }, { tail })
}

fun <A> Stream<A>.toList(): List<A> {
    fun <A> loop(xs: List<A>, stream: Stream<A>): List<A> =
        when (stream) {
            is Empty -> xs
            is Cons -> loop(xs + stream.head(), stream.tail())
        }
    return loop(emptyList(), this)
}

fun <A> Stream<A>.take(n: Int): Stream<A> {
    fun <A> go(n: Int, stream: Stream<A>): Stream<A> =
        when (stream) {
            is Cons ->
                if (n == 0) Empty
                else cons(stream.head, { go(n - 1, stream.tail()) })

            is Empty -> Empty
        }
    return go(n, this)
}

fun <A> Stream<A>.forAll(p: (A) -> Boolean): Boolean =
    when (this) {
        is Empty -> true
        is Cons -> p(this.head()) && this.tail().forAll(p)
    }

fun ones() : Stream<Int> = cons({ 1 }, { ones() })

fun from(n: Int): Stream<Int> = cons({n}, {from (n + 1)})

fun fibs(): Stream<Int> {
    fun go(a : Int, b : Int) : Stream<Int> = cons({a}, {go(b, a + b)})
    return go(0, 1)
}