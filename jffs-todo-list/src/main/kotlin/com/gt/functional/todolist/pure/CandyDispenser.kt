package com.gt.functional.todolist.pure

import com.gt.functional.todolist.pure.IsSorted.tail

sealed class Input
/*
* Inserting a coin into a locked machine will cause it to unlock if there’s any candy left.
* Turning the knob on an unlocked machine will cause it to dispense candy and become locked.
* Turning the knob on a locked machine or inserting a coin into an unlocked machine does nothing.
* A machine that’s out of candy ignores all inputs
*/

data class Machine(
    val locked: Boolean,
    val candies: Int,
    val coins: Int
)

data object Coin : Input()

data object Turn : Input()

fun simulateMachine(inputs: List<Input>): State<Machine, Pair<Int, Int>> {
    fun simulate(machine: Machine, xs : List<Input>) : Pair<Pair<Int, Int>, Machine> =
        when (xs.isEmpty()) {
            true -> Pair(machine.coins, machine.candies) to machine
            false -> when (machine.candies == 0) {
                true -> Pair(machine.coins, 0) to machine
                false -> when (xs.first()) {
                    Coin -> if (machine.locked) {
                        simulate(Machine(false, machine.candies, machine.coins + 1), xs.tail)
                    } else {
                        simulate(Machine(true, machine.candies, machine.coins), xs.tail)
                    }
                    Turn -> if (!machine.locked) {
                        simulate(Machine(true, machine.candies - 1, machine.coins), xs.tail)
                    } else {
                        simulate(Machine(true, machine.candies, machine.coins), xs.tail)
                    }
                }
            }
        }

    return { machine -> simulate(machine, inputs) }
}

fun simulateMachineWithFold(inputs: List<Input>) : State<Machine, Pair<Int, Int>> {
    return {machine ->
        val finalMachine = inputs.fold(machine) {m, input ->
            when {
                m.candies == 0 -> m
                input is Coin && m.locked -> m.copy(locked = false, coins = m.coins + 1)
                input is Turn && !m.locked -> m.copy(locked = true, candies = m.candies - 1)
                else -> m
            }
        }
        Pair(finalMachine.coins to finalMachine.candies, finalMachine)
    }
}


fun main() {
    val machineState1 = simulateMachine(listOf(Coin, Turn, Coin, Turn, Coin, Turn, Coin, Turn))
    val (value1, _) = machineState1(Machine(true, 5, 10))
    println(value1)
    val machineState2 = simulateMachine(listOf(Coin, Turn, Coin, Turn, Coin, Turn, Coin, Turn))
    val (value2, _) = machineState2(Machine(true, 5, 10))
    println(value2)
}