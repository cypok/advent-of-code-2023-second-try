
fun main() {
    fun processOneCard(line: String): Int {
        // line example:
        // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        val nums = line.split(Regex(" +")).asSequence()
        val winning = nums.drop(2).takeWhile { it != "|" }.toSet()
        return nums.dropWhile { it != "|" }.drop(1).count { it in winning }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            // 0 -> 0, n -> 2^(n-1)
            (1 shl processOneCard(line)) shr 1
        }
    }

    fun part2(input: List<String>): Int {
        return input.fold(Pair(0, listOf<Int>())) { acc, line ->
            val (total, bonuses) = acc
            val wonPerCard = processOneCard(line)
            val cardsProcessed = 1 + bonuses.size
            val newTotal = total + cardsProcessed
            val reducedOldBonuses = bonuses.map { it - 1 }
            val addedBonuses = (0 until cardsProcessed).map { wonPerCard }
            val newBonuses = (reducedOldBonuses + addedBonuses).filter { it != 0 }
            Pair(newTotal, newBonuses)
        }.first
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
