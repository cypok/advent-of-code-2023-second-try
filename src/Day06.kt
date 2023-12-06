fun main() {
    fun process(races: List<Pair<Long, Long>>): Long {
        return races.fold(1) { acc, (time, distance) ->
            acc * (0 until time).count { hold ->
                val speed = hold
                speed * (time - hold) > distance
            }
        }
    }

    fun part1(input: List<String>): Long {
        val (times, distances) = input.map {
            it.split(Regex(" +")).drop(1).map { it.toLong() }
        }
        return process(times zip distances)
    }

    fun part2(input: List<String>): Long {
        val (time, distance) = input.map {
            it.substringAfter(":").replace(" ", "").toLong()
        }
        return process(listOf(Pair(time, distance)))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
