package com.gt.functional.todolist

object RPNCalculator {
    fun calc(expression: String): Int =
        calc2(FunStack(), expression.split(" "), 0)

    private fun calc2(stack: FunStack<Int>, tokens: List<String>, index: Int): Int {
        if (index >= tokens.size) {
            return stack.pop().second
        }

        val token = tokens[index]
        return if (!isOperator(token)) {
            calc2(pushOnStack(token, stack), tokens, index + 1)
        } else {
            calc2(applyOperation(token, stack), tokens, index + 1)
        }
    }

    private fun pushOnStack(s: String, stack: FunStack<Int>) = stack.push(Integer.valueOf(s))

    private fun applyOperation(s: String, stack: FunStack<Int>): FunStack<Int> {
        val (stack2, num1) = stack.pop()
        val (stack3, num2) = stack2.pop()
        return when (s) {
            "+" -> stack3.push(num1 + num2)
            "-" -> stack3.push(num2 - num1)
            "/" -> stack3.push(num2 / num1)
            else -> stack3.push(num2 * num1)
        }
    }

    private fun isOperator(o: String): Boolean =
        when (o) {
            "+", "-", "/", "*" -> true
            else -> false
        }

}
