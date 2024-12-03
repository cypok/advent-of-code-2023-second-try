package year2024

import utils.*

// Task description:
//   https://adventofcode.com/2024/day/3

fun main() = test(
    ::solve1,
    ::solve2,
)

private val INSTRUCTION = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()

private fun solve1(input: List<String>): Long =
    INSTRUCTION.findAll(input.joinToString()).sumOf { it.calculate() }

private fun solve2(input: List<String>): Long {
    var enabled = true
    val line = input.joinToString()
    var sum = 0L
    for (i in 0..line.length) {
        if (line.startsWith("do()", i)) {
            enabled = true
        } else if (line.startsWith("don't()", i)) {
            enabled = false
        } else if (enabled) {
            sum += INSTRUCTION.matchAt(line, i)?.calculate() ?: 0
        }
    }
    return sum
}

private fun MatchResult.calculate(): Long =
    this.groupValues.drop(1).productOf { it.toLong() }