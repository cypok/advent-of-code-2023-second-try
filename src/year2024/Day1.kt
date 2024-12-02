package year2024

import utils.numbers
import utils.test
import kotlin.math.absoluteValue

// Task description:
//   https://adventofcode.com/2024/day/1

fun main() = test(
    ::solve1,
    ::solve2,
)

private fun solve1(input: List<String>): Long {
    val (xs, ys) = parse(input)
    return xs.sorted().zip(ys.sorted())
        .sumOf { (x, y) -> (x - y).absoluteValue.toLong() }
}

private fun solve2(input: List<String>): Long {
    val (xs, ys) = parse(input)
    return xs.sumOf { x -> x.toLong() * ys.count { y -> x == y } }
}

private fun parse(input: List<String>) =
    input
        .map { it.numbers().let { (a, b) -> a to b } }
        .unzip()
