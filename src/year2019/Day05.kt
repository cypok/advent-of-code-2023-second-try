package year2019

import utils.*

// Task description:
//   https://adventofcode.com/2019/day/5

fun main() = runAoc {
    solution {
        val program = lines.single().numbersAsInts()
        val pc = IntCodeComputer(program)
        if (isPart1) {
            val result = pc.interpret(1)
            check(result.dropLast(1).all { it == 0 })
            result.last()
        } else {
            pc.interpret(5).single()
        }
    }
}