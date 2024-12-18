package com.gt.functional.todolist.exceptions

import kotlin.math.pow


sealed class Option<out A>

data class Some<out A>(val get: A) : Option<A>()

data object None : Option<Nothing>()

fun mean(xs: List<Double>): Option<Double> =
    if (xs.isEmpty()) None else
        Some(xs.sum() / xs.size)

fun <A, B> Option<A>.map(f: (A) -> B): Option<B> =
    when (this) {
        is Some -> Some(f(this.get))
        is None -> None
    }

fun <A> Option<A>.getOrElse(default: () -> A): A =
    when (this) {
        is None -> default()
        is Some -> this.get
    }

fun <A, B> Option<A>.flatMap(f: (A) -> Option<B>): Option<B> =
    this.map(f).getOrElse { None }

fun <A> Option<A>.orElse1(ob: Option<A>): Option<A> =
    when (this) {
        is None -> ob
        is Some -> this
    }

fun <A> Option<A>.orElse2(ob: Option<A>): Option<A> =
    this.map { Some(it) }.getOrElse { ob }

fun <A> Option<A>.filter(f: (A) -> Boolean): Option<A> =
    this.flatMap { if (f(it)) Some(it) else None }

fun variance(xs: List<Double>): Option<Double> =
    mean(xs)
        .map { m -> xs.sumOf { x -> (x - m).pow(2) } / xs.size }

fun <A, B, C> map2(a: Option<A>, b: Option<B>, f: (A, B) -> C): Option<C> =
    when (a) {
        is None -> None
        is Some -> when (b) {
            is None -> None
            is Some -> Some(f(a.get, b.get))
        }
    }

fun <A> sequence(xs: List<Option<A>>): Option<List<A>> =
    xs.foldRight(Some(emptyList()))
        {
            oa1, oa2 -> when (oa1) {
                is None -> oa2
                is Some -> oa2.map { x -> x + oa1.get }
            }
        }

fun <A, B> catches(f : () -> (B) ) : Option<B> =
    try {
        Some(f())
    } catch (e : Throwable) {
        None
    }