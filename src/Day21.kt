import Dir.*
import kotlin.math.min

fun main() = test(
    { solveFinite(it, 64) },
    { solveInfinite(it, 26_501_365) },
)

private fun solveFinite(input: List<String>, steps: Int): Long {
    val map = StringArray2D(input)
    val moves = Array(map.height) { Array(map.width) { -1 } }

    for (i in 0 until map.height) {
        for (j in 0 until map.width) {
            if (map[i, j] == 'S') {
                moves[i, j] = 0
            }
        }
    }

    fun step(dir: Dir, base: Pair<Int, Int>, s: Int) {
        val next = moveInDir(base, dir)
        val ch = map.getOrNull(next) ?: return
        if (ch == '#') return
        val old = moves[next]
        assert(old != s)
        if (old != s + 1) {
            moves[next] = s + 1
        }
    }

    repeat(steps) { s ->
        for (i in 0 until map.height) {
            for (j in 0 until map.width) {
                if (moves[i, j] == s) {
                    val base = Pair(i, j)
                    step(UP, base, s)
                    step(LEFT, base, s)
                    step(DOWN, base, s)
                    step(RIGHT, base, s)
                }
            }
        }
    }

    return moves.sumOf { it.sumOf { if (it == steps) 1L else 0L } }
}


private fun solveInfinite(input: List<String>, totalSteps: Int): Long {
    val map = StringArray2D(input)

    val enoughExtension = 5
    val enoughSteps = map.width * enoughExtension * 2

    val multiplier = 1 + 3 * enoughSteps / min(map.width, map.height)
    val marks = Array(map.height * multiplier) { Array(map.width * multiplier) { -1 } }

    fun posForMap(p: Pair<Int, Int>) = Pair(p.first.mod(map.height), p.second.mod(map.width))

    for (i in 0 until map.height) {
        for (j in 0 until map.width) {
            if (map[i, j] == 'S') {
                marks[i + multiplier / 2 * map.height, j + multiplier / 2 * map.width] = 0
            }
        }
    }

    fun step(dir: Dir, base: Pair<Int, Int>, s: Int): Boolean {
        val next = moveInDir(base, dir)
        val ch = map[posForMap(next)]
        if (ch == '#') return false
        val old = marks[next]
        assert(old != s)
        return if (old != s + 1) {
            marks[next] = s + 1
            true
        } else {
            false
        }
    }

    // Just choose one of the directions.
    var leftLimitNext = multiplier/2 * map.width + 1

    val limitHitEvents = mutableListOf<Pair<Int, Long>>()

    for (s in 0 until enoughSteps) {
        var reachedLimit = false
        for (i in marks.indices) {
            for (j in marks[i].indices) {
                val base = Pair(i, j)
                if (marks[base] == s) {
                    step(UP, base, s)
                    if (step(LEFT, base, s) && i == leftLimitNext) {
                        assert(!reachedLimit)
                        reachedLimit = true
                        leftLimitNext -= map.width
                    }
                    step(DOWN, base, s)
                    step(RIGHT, base, s)
                }
            }
        }

        if (reachedLimit) {
            val past = s + 1
            val count = marks.sumOf { it.sumOf { if (it == s + 1) 1L else 0L } }
            limitHitEvents += past to count
            if (limitHitEvents.size == enoughExtension) {
                break
            }
        }
    }

    if (limitHitEvents.size < 5) {
        throw IllegalStateException("not enough steps!")
    }

    val velocities = limitHitEvents.map { it.second }.derivative()
    val accelerations = velocities.derivative()
    val acceleration = accelerations.first()
    assert(accelerations.all { it == acceleration })

    val stepStart = limitHitEvents.first().first
    val stepDeltas = limitHitEvents.map { it.first.toLong() }.derivative()
    val stepDelta = stepDeltas.first().toIntExact()
    assert(stepDeltas.all { it.toIntExact() == stepDelta })

    assert((totalSteps - stepStart) % stepDelta == 0) {
        "otherwise it's easier to tune leftLimitNext, rather than write more code" }

    var curStep = limitHitEvents.last().first
    var curCount = limitHitEvents.last().second
    var curVelocity = velocities.last()

    while (curStep < totalSteps) {
        curVelocity += acceleration
        curCount += curVelocity
        curStep += stepDelta
    }
    assert(curStep == totalSteps)
    return curCount
}

private fun List<Long>.derivative() = windowed(2).map { it[1] - it[0] }
