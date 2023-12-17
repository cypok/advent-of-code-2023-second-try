import Dir.*

fun main() = test(
    ::solve1,
    ::solve2,
)

private fun solve1(input: List<String>): Long {
    val map = StringArray2D(input)
    return calculateEnergy(map, Pair(0, 0), RIGHT)
}

private fun solve2(input: List<String>): Long {
    val map = StringArray2D(input)
    return sequence {
        for (i in 0 until map.height) {
            yield(calculateEnergy(map, Pair(i, 0), RIGHT))
            yield(calculateEnergy(map, Pair(i, map.width - 1), LEFT))
        }
        for (j in 0 until map.width) {
            yield(calculateEnergy(map, Pair(0, j), DOWN))
            yield(calculateEnergy(map, Pair(map.height - 1, j), UP))
        }
    }.max()
}

private fun calculateEnergy(map: StringArray2D, startPos: Pair<Int, Int>, startDir: Dir): Long {
    val beams = Array(map.height) { Array(map.width) { mutableSetOf<Dir>() } }

    tailrec fun traverse(pos: Pair<Int, Int>, dir: Dir) {
        val (i, j) = pos
        if (map.getOrNull(i, j) == null) return
        if (dir in beams[i, j]) return
        beams[i, j] += dir

        val elem = map[i, j]
        val dirs =
            when (elem) {
                '.' -> listOf(dir)

                '/' -> listOf(when (dir) {
                    RIGHT -> UP
                    LEFT -> DOWN
                    UP -> RIGHT
                    DOWN -> LEFT
                })

                '\\' -> listOf(when (dir) {
                    RIGHT -> DOWN
                    LEFT -> UP
                    UP -> LEFT
                    DOWN -> RIGHT
                })

                '|' -> when (dir) {
                    UP, DOWN -> listOf(dir)
                    LEFT, RIGHT -> listOf(dir.left, dir.right)
                }

                '-' -> when (dir) {
                    LEFT, RIGHT -> listOf(dir)
                    UP, DOWN -> listOf(dir.left, dir.right)
                }

                else -> throw IllegalStateException()
            }

        if (dirs.size == 2) {
            @Suppress("NON_TAIL_RECURSIVE_CALL")
            traverse(moveInDir(pos, dirs[1]), dirs[1])
        } else {
            assert(dirs.size == 1)
        }
        traverse(moveInDir(pos, dirs[0]), dirs[0])
    }

    traverse(startPos, startDir)
    return beams.sumOf { it.sumOf { if (it.isEmpty()) 0L else 1L } }
}
