package year2024

import utils.*
import java.util.Comparator

// Task description:
//   https://adventofcode.com/2024/day/5

fun main() = test(
    { solve(it, fixing = false) },
    { solve(it, fixing = true) },
)

private fun solve(input: List<String>, fixing: Boolean): Long {
    val (rulesLines, updates) = input.splitByEmptyLines().toList()
    val rules = rulesLines.map {
        it.numbers().let { (a, b) -> a to b }
    }.toSet()

    return updates.sumOf { update ->
        val pages = update.numbers()
        val right = isRightOrder(pages, rules)
        if (fixing) {
            if (right) {
                0
            } else {
                pages.sortedWith(comparatorByRules(rules)).middle()
            }
        } else {
            if (right) {
                pages.middle()
            } else {
                0
            }
        }.toLong()
    }
}

private fun isRightOrder(pages: List<Long>, rules: Set<Pair<Long, Long>>): Boolean {
    for (i in 0..<pages.size) {
        for (j in i+1..<pages.size) {
            if ((pages[j] to pages[i]) in rules) {
                return false
            }
        }
    }
    return true
}

private fun comparatorByRules(rules: Set<Pair<Long, Long>>) =
    Comparator<Long> { x, y ->
        if ((x to y) in rules) {
            -1
        } else if ((y to x) in rules) {
            1
        } else {
            0
        }
    }