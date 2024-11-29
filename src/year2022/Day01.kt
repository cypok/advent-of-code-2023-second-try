package year2022

import utils.split
import utils.test

fun main() = test(
    { solve(it, 1) },
    { solve(it, 3) },
)

private fun solve(input: List<String>, topCount: Int): Long =
    input.split("")
        .map { it.sumOf { it.toLong() } }
        .sortedDescending()
        .take(topCount)
        .sum()
