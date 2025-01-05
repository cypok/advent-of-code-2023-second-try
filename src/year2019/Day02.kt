package year2019

import utils.*

// Task description:
//   https://adventofcode.com/2019/day/2

fun main() = runAoc {
    solution {
        val program = lines.single().numbersAsInts()
        fun run(noun: Int, verb: Int): Int {
            val pc = IntCodeComputer(program)
            pc[1] = noun
            pc[2] = verb
            pc.interpret()
            return pc[0]
        }

        if (isPart1) {
            run(12, 2)

        } else {
            for (noun in 0..99) {
                for (verb in 0..99) {
                    if (run(noun, verb) == 19690720) {
                        return@solution 100 * noun + verb
                    }
                }
            }
            shouldNotReachHere()
        }
    }
}