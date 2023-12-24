import Dir.*
import kotlin.math.max

fun main() = test(
    { solve(it, true) },
    { solve(it, false) },
)

private val SLOPES = mapOf(
    '^' to UP,
    '<' to LEFT,
    'v' to DOWN,
    '>' to RIGHT,
)

private fun solve(input: List<String>, respectSlopes: Boolean): Long {
    val map = StringArray2D(input)

    val start = 0 x 1
    val finish = (map.height - 1) x (map.width - 2)
    check(map[start] == '.')
    check(map[finish] == '.')

    tailrec fun traverse(pos: Point, dir: Dir, len: Long, visited: MutableSet<Point>, alternativeLen: Long): Long {
        if (pos == finish) return max(len, alternativeLen)

        fun canGoTo(nextPos: Point, dir: Dir): Boolean {
            val ch = map[nextPos]

            if (ch == '#') return false
            if (nextPos in visited) return false

            if (respectSlopes) {
                for ((slCh, slDir) in SLOPES) {
                    if (ch == slCh && slDir == dir.opposite) return false
                }
            }

            return true
        }

        val slopedDir = if (respectSlopes) SLOPES[map[pos]] else null
        val nextDirs =
            if (slopedDir != null) {
                listOf(slopedDir)
            } else {
                listOf(dir, dir.left, dir.right)
                    .filter { canGoTo(pos.moveInDir(it), it) }
            }

        if (nextDirs.isEmpty()) return alternativeLen

        val firstDir = nextDirs.first()
        val forkedLen =
            if (nextDirs.size == 1) {
                // Single track case.
                alternativeLen
            } else {
                val forkedDirs = nextDirs.drop(1)

                // We can skip this step if we are on a single track.
                visited += pos

                val forkedLen = forkedDirs
                    // We must prepare a new set for every fork, except the first one.
                    .map { Pair(it, visited.toMutableSet()) }
                    .maxOf { (nextDir, nextVisited) ->
                        @Suppress("NON_TAIL_RECURSIVE_CALL")
                        traverse(
                            pos.moveInDir(nextDir), nextDir,
                            len + 1,
                            nextVisited,
                            alternativeLen
                        )
                    }
                assert(forkedLen >= alternativeLen)
                forkedLen
            }

        return traverse(pos.moveInDir(firstDir), firstDir,
            len + 1,
            // We can reuse this mutable set if we are going by single track.
            visited,
            forkedLen)
    }

    return traverse(start, DOWN, 0, mutableSetOf(), Long.MIN_VALUE)
}
