package com.gt.functional.todolist.datastructure

/*
  A is a covariant
  out A -> List<Nothing> is a subtype of List<
 */
sealed class LinkedList<out A> {
    companion object {
        /*
        Converting a variable list of arguments provided in the parameter into
        a list
         */
        fun <A> of(vararg aa: A): LinkedList<A> {
            val tail = aa.sliceArray(1 until aa.size)
            return if (aa.isEmpty()) Nil else Cons(aa[0], of(*tail))
        }
    }
}

object Nil : LinkedList<Nothing>()

fun <A> emptyLinkedList() : LinkedList<A> = Nil

data class Cons<out A>(val head: A, val tail: LinkedList<A>) : LinkedList<A>()

fun sum(xs: LinkedList<Int>): Int = foldRight(xs, 1) { a, b -> a + b }

fun product(xs: LinkedList<Int>): Int = foldRight(xs, 1) { a, b -> a * b }

fun length(xs: LinkedList<Int>): Int = foldRight(xs, 0) { _, b -> 1 + b }

fun <A, B> foldRight(xs: LinkedList<A>, z: B, f: (A, B) -> B): B =
    when (xs) {
        is Nil -> z
        is Cons -> f(xs.head, foldRight(xs.tail, z, f))
    }

tailrec fun <A, B> foldLeft(xs: LinkedList<A>, z: B, f: (B, A) -> B): B =
    when (xs) {
        is Nil -> z
        is Cons -> foldLeft(xs.tail, f(z, xs.head), f)
    }

fun <A> reverse(xs: LinkedList<A>): LinkedList<A> {
    tailrec fun loop(list: LinkedList<A>, tailingList: LinkedList<A>): LinkedList<A> =
        when (list) {
            is Nil -> tailingList
            is Cons -> loop(list.tail, Cons(list.head, tailingList))
        }
    return loop(xs, Nil)
}

fun <A> reverseFold(xs: LinkedList<A>) : LinkedList<A> =
    foldLeft(xs, emptyLinkedList()) { tail, b -> Cons(b, tail) }

fun sumLeft(xs : LinkedList<Int>) : Int =
    foldLeft(xs, 0) {a, b -> a + b}

fun productLeft(xs : LinkedList<Int>) : Int =
    foldLeft(xs, 1) {a, b -> a * b}

fun listLengthLeft(xs : LinkedList<Int>) : Int =
    foldLeft(xs, 0) {a, _ -> a + 1}

fun increment(xs : LinkedList<Int>) : LinkedList<Int> =
    when (xs) {
        is Nil -> Nil
        is Cons -> Cons(xs.head + 1, increment(xs.tail))
    }

fun incrementFold(xs : LinkedList<Int>) : LinkedList<Int> =
    foldRight(xs, emptyLinkedList()) { x, y -> Cons(x + 1, y)}

fun toString(xs : LinkedList<Double>) : LinkedList<String> =
    foldRight(xs, emptyLinkedList()) { x, y -> Cons(x.toString(), y)}

fun <A, B> map(xs: LinkedList<A>, f: (A) -> B): LinkedList<B> =
    foldRight(xs, emptyLinkedList()) {x, y -> Cons(f(x), y)}

fun <A> filter(xs: LinkedList<A>, f: (A) -> Boolean): LinkedList<A> =
    foldRight(xs, emptyLinkedList()) {x, y -> if (f(x)) Cons(x, y) else y}

fun <A> append(xs : LinkedList<A>, ys: LinkedList<A>) : LinkedList<A> =
    foldRight(xs, ys) {a, b -> Cons(a, b)}

fun <A, B> flatMap(xs : LinkedList<A>, f : (A) -> LinkedList<B>) : LinkedList<B> =
    when (xs) {
        is Nil -> Nil
        is Cons -> append(f(xs.head), flatMap(xs.tail, f))
    }

fun add(xs : LinkedList<Int>, ys : LinkedList<Int>) : LinkedList<Int> =
    when (xs) {
        is Nil -> Nil
        is Cons -> when (ys) {
            is Nil -> Nil
            is Cons -> Cons(xs.head + ys.head, add(xs.tail, ys.tail))
        }
    }

fun <A> hasSubsequence(xs : LinkedList<A>, sub : LinkedList<A>) : Boolean {
    fun <A, B> same(xs: LinkedList<A>, ys: LinkedList<B>): Boolean {
        return when (ys) {
            is Nil -> true
            is Cons -> when (xs) {
                is Nil -> true
                is Cons ->
                    xs.head == ys.head && same(xs.tail, ys.tail)
            }
        }
    }

    return when (xs) {
        is Nil -> false
        is Cons -> when (sub) {
            is Nil -> false
            is Cons -> if (xs.head == sub.head && same(xs.tail, sub.tail)) true else
                hasSubsequence(xs.tail, sub)
        }
    }
}


fun <A> zipWith(xs : LinkedList<A>, ys : LinkedList<A>, f: (A, A) -> A) : LinkedList<A> =
    when (xs) {
        is Nil -> Nil
        is Cons -> when (ys) {
            is Nil -> Nil
            is Cons -> Cons(f(xs.head, ys.head), zipWith(xs.tail, ys.tail, f))
        }
    }

fun main() {
    val list = Cons(1, Cons(2, Cons(3, Cons(7, Nil))))
    println(list)
    println(reverse(list))
    println(reverseFold(list))
    println(sumLeft(list))
    println(productLeft(list))
    println(listLengthLeft(list))
    println(increment(list))
    println(incrementFold(list))
    val doubleList = Cons(1.0, Cons(2.0, Cons(3.0, Cons(7.0, Nil))))
    println(toString(doubleList))
    println(map(list){a -> a * 2})
    println(filter(list){a -> a % 2 == 0})
    val list2 = Cons(10, Cons(5, Cons(9, Cons(20, Nil))))
    println(append(list, list2))
    println(flatMap(list) { i -> Cons(i, Cons(i, Nil)) })
    println(hasSubsequence(
        list,
        Cons(1, Cons(2, Nil))
    ))
    println(hasSubsequence(
        list,
        Cons(1, Cons(2, Cons(3, Nil)))
    ))
    println(hasSubsequence(
        list,
        Cons(1, Nil)
    ))
    println(hasSubsequence(
        list,
        Cons(7, Nil)
    ))
    println(hasSubsequence(
        list,
        Cons(1, Cons(2, Cons(4, Nil)))
    ))
}

