package year2023

import utils.*

fun main() = test(
    { year2023.solve(it, 1) },
    { year2023.solve(it, 5) },
)

private fun <T> expand(xs: Iterable<T>, expansion: Int, separator: T?): LList<T>? =
    (0 until expansion)
        .map { xs }
        .reduce { x, y -> if (separator != null) x + separator + y else x + y }
        .toLList()

private fun solve(input: List<String>, expansion: Int): Long {
    return input.sumOf { line ->
        val pattern = year2023.expand(line.substringBefore(' ').asIterable(), expansion, '?')
        val groups = year2023.expand(line.substringAfter(' ').split(',').map { it.toInt() }, expansion, null)

        year2023.countVariants(mutableMapOf(), year2023.skipSpaces(pattern), null, groups)
    }
}

private fun <A, R> memoize(cache: MutableMap<A, R>, args: A, calcFunc: () -> R): R =
    cache.getOrPut(args) { calcFunc() }

private tailrec fun skipSpaces(pattern: LList<Char>?): LList<Char>? =
    if (pattern == null || pattern.head != '.') pattern else year2023.skipSpaces(pattern.tail)

private fun countVariants(
    cache: MutableMap<Any, Long>,
    pattern: LList<Char>?, curGroupRemaining: Int?, groups: LList<Int>?
): Long =
    year2023.memoize(cache, Triple(pattern, curGroupRemaining, groups)) {
        assert(year2023.skipSpaces(pattern) == pattern)
        if (pattern == null) {
            // recursion termination case
            if (groups == null && (curGroupRemaining == null || curGroupRemaining == 0)) {
                1L
            } else {
                0L
            }
        } else if (curGroupRemaining != null) {
            // trivial recursion case, when the next character is predictable
            if (curGroupRemaining == 0) {
                // . must be the next char.
                when (pattern.head) {
                    '#' -> 0L
                    '.', '?' -> year2023.countVariants(cache, year2023.skipSpaces(pattern.tail), null, groups)
                    else -> throw IllegalStateException()
                }
            } else {
                assert(curGroupRemaining > 0)
                // # must be the next char.
                when (pattern.head) {
                    '.' -> 0L
                    '#', '?' -> year2023.countVariants(cache, pattern.tail, curGroupRemaining - 1, groups)
                    else -> throw IllegalStateException()
                }
            }
        } else {
            // non-trivial recursion case, when we have to try both values for the next character
            fun countAsHashStart() =
                if (groups == null) 0
                else year2023.countVariants(cache, pattern.tail, groups.head - 1, groups.tail)

            when (pattern.head) {
                '#' -> countAsHashStart()
                '?' -> {
                    val asHash = countAsHashStart()
                    val asSpace = year2023.countVariants(cache, year2023.skipSpaces(pattern.tail), null, groups)
                    asHash + asSpace
                }

                '.' -> throw AssertionError()
                else -> throw IllegalStateException()
            }
        }
    }
