package year2023

import utils.*
import kotlin.math.max

data class RGB(
    val red: Int,
    val green: Int,
    val blue: Int
)

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val match = Regex("""Game (\d+): (.*)""").matchEntire(line)!!
            val gameId = match.groupValues[1].toInt()
            val sets = match.groupValues[2]
            val isPossible = sets.splitToSequence("; ").all { set ->
                set.splitToSequence(", ").all { group ->
                    val numAndColor = group.split(" ")
                    val num = numAndColor[0].toInt()
                    when (numAndColor[1]) {
                        "red" -> num <= 12
                        "green" -> num <= 13
                        "blue" -> num <= 14
                        else -> throw IllegalStateException()
                    }
                }
            }
            if (isPossible) gameId else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val sets = line.substringAfter(": ")
            val res = sets.splitToSequence("; ").fold(RGB(0, 0, 0)) { acc, set ->
                set.splitToSequence(", ").fold(acc) { acc, group ->
                    val numAndColor = group.split(" ")
                    val num = numAndColor[0].toInt()
                    when (numAndColor[1]) {
                        "red"   -> acc.copy(red   = max(acc.red,   num))
                        "green" -> acc.copy(green = max(acc.green, num))
                        "blue"  -> acc.copy(blue  = max(acc.blue,  num))
                        else -> throw IllegalStateException()
                    }
                }
            }
            res.red * res.blue * res.green
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
