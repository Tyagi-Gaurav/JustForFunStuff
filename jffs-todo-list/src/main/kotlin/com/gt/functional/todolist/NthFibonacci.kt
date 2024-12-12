package com.gt.functional.todolist

import com.gt.functional.todolist.NthFibonacci.fib

object NthFibonacci {
    fun fib(num : Int) : Int {
        tailrec fun go(counter: Int, num1 : Int, num2: Int) : Int {
            if (counter == num) {
                return num2
            } else {
                return go(counter + 1, num2, num1 + num2)
            }
        }

        return when (num) {
            1 -> 0
            2 -> 1
            else -> go(2, 0, 1)
        }
    }
}

fun main() {
    println(fib(1))
    println(fib(2))
    println(fib(3))
    println(fib(4))
    println(fib(5))
    println(fib(9))
}