package com.gt.functional.todolist

object PartialFunction {
    private fun <A, B, C> partial1(a: A, f: (A, B)-> C): (B)-> C = { b -> f(a, b)}

/*
    Currying, which converts a function f of two arguments into a function with one argument that partially applies f.
    The operator -> associates to the right so we can right (A) -> ((B) -> C) as (A) -> (B) -> C
*/
    fun <A, B, C> curry(f: (A, B)-> C): (A) -> (B)-> C = {a: A -> partial1(a, {a1 : A, b1: B -> f(a1, b1)}) }

    fun <A, B, C> uncurry(f: (A) -> (B) -> C) : (A, B) -> C = {a : A, b : B -> f(a).invoke(b)}

    fun <A, B, C> compose(f: (B)-> C, g: (A)-> B): (A)-> C = { a -> f(g(a))}

}