package com.gt.functional.todolist.pure

interface RNG {
    fun nextInt(): Pair<Int, RNG>
}