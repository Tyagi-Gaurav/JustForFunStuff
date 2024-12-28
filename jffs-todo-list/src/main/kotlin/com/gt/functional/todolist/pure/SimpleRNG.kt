package com.gt.functional.todolist.pure

import kotlin.math.abs

class SimpleRNG(val seed: Long) : RNG {
    override fun nextInt(): Pair<Int, RNG> {
        val newSeed = (seed * 0x5DEECE66DL + 0xBL) and ((1L shl 48) - 1)
        val nextRNG = SimpleRNG(newSeed)
        val n = (newSeed ushr 16).toInt()
        return Pair(n, nextRNG)
    }
}

fun nonNegativeInt(): Rand<Int> = { rng ->
    val (value, newRng) = rng.nextInt()
    if (value == Int.MIN_VALUE) {
        abs(value + 1) to newRng
    } else if (value < 0) {
        abs(value) to newRng
    } else {
        value to newRng
    }
}

//a randomly generated A
typealias Rand<A> = (RNG) -> Pair<A, RNG>

fun <A> unit(a: A): Rand<A> = { rng -> a to rng }

fun <A, B> rngmap(s: Rand<A>, f: (A) -> B): Rand<B> =
    { rng ->
        val (a, newRng) = s(rng)
        f(a) to newRng
    }

fun <A, B, C> rngmap2(ra : Rand<A>, rb: Rand<B>, f: (A, B) -> C) : Rand<C> =
    {
        rng ->
            val (a, newRng1) = ra(rng)
            val (b, newRng2) = rb(newRng1)
            f(a, b) to newRng2
    }

fun double(): Rand<Double> =
        rngmap(nonNegativeInt()) {x : Int -> x.toDouble() / Int.MAX_VALUE}

fun nonNegativeEven(): Rand<Int> =
    rngmap(nonNegativeInt()) { x: Int -> x - x % 2 }

fun main() {
    val rng = SimpleRNG(42)
    val (n1, rng2) = rng.nextInt()
    println(n1)
    val (n2, rng3) = rng2.nextInt()
    println(n2)
    println(nonNegativeEven()(rng3).first)
    println(nonNegativeEven()(rng3).first)
//    val (n3, rng4) = nonNegativeInt(rng3)
//    println(n3)
//    val (n4, rng5) = nonNegativeInt(rng4)
//    println(n4)
//    val (n5, rng6) = double(rng5)
//    println(n5)
//    val (n6, rng7) = double(rng6)
//    println(n6)
}