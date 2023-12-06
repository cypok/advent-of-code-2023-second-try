fun main() = test(::part1, ::part2)

private fun process(races: List<Pair<Long, Long>>): Long {
    return races.fold(1) { acc, (time, distance) ->
        acc * (0 until time).count { hold ->
            val speed = hold
            speed * (time - hold) > distance
        }
    }
}

private fun part1(input: List<String>): Long {
    val (times, distances) = input.map {
        it.split(Regex(" +")).drop(1).map { it.toLong() }
    }
    return process(times zip distances)
}

private fun part2(input: List<String>): Long {
    val (time, distance) = input.map {
        it.substringAfter(":").replace(" ", "").toLong()
    }
    return process(listOf(Pair(time, distance)))
}
