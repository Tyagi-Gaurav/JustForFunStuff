package com.gt.functional.todolist

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FunStackTest {
    @Test
    fun `push into the stack`() {
        val stack1 = FunStack<Char>()
        val stack2 = stack1.push('A')

        assertThat(stack1.size()).isZero()
        assertThat(stack2.size()).isOne()
    }

    @Test
    fun `pop from stack`() {
        val stack1 = FunStack<Char>()
        val stack2 = stack1.push('A')
        val stack3 = stack2.push('B')
        val (stack4, item) = stack3.pop();

        assertThat(stack1.size()).isZero()
        assertThat(stack2.size()).isOne()
        assertThat(stack3.size()).isEqualTo(2)
        assertThat(stack4.size()).isEqualTo(1)
        assertThat(item).isEqualTo('B')
    }
}