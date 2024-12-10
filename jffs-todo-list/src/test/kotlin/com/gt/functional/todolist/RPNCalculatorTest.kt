package com.gt.functional.todolist

import com.gt.functional.todolist.RPNCalculator.calc
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RPNCalculatorTest {
    @Test
    fun calculate() {
        assertThat(calc("4 5 +")).isEqualTo(9)
        assertThat(calc("6 2 /")).isEqualTo(3)
        assertThat(calc("5 6 2 1 + / *")).isEqualTo(10)
        assertThat(calc("2 5 * 4 + 3 2 * 1 + /")).isEqualTo(2)
    }
}