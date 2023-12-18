fun main() = test(
    { solve(2_000, it.map(::parseLinePart1)) },
    { solve(20_000, it.map(::parseLinePart2)) },
)

private val INPUT_REGEX_PART1 = """(.) (\d+) .*""".toRegex()

private fun parseLinePart1(line: String): Pair<Int, Dir> {
    val (dirCh, len) = INPUT_REGEX_PART1.matchEntire(line)!!.destructured
    return Pair(len.toInt(), Dir.fromChar(dirCh[0]))
}

private val INPUT_REGEX_PART2 = """. \d+ \(#(.....)(.)\)""".toRegex()
private val dirCodeToDir = "RDLU".map { Dir.fromChar(it) }

private fun parseLinePart2(line: String): Pair<Int, Dir> {
    val (lenHex, dirCode) = INPUT_REGEX_PART2.matchEntire(line)!!.destructured
    val dir = dirCodeToDir[dirCode.toInt()]
    return Pair(lenHex.toInt(16), dir)
}


private fun solve(mapSize: Int, commands: List<Pair<Int, Dir>>): Long {
    // Hope, it would be enough.
    val map = Array(mapSize) { Array(mapSize) { false } }

    // Count all filled cells.
    var fill = 0L
    fun visit(pos: Pair<Int, Int>): Boolean {
        if (map[pos]) return false // Already visited.
        map[pos] = true
        fill++
        return true
    }

    // Detect an orientation of the cycle.
    var firstDir: Dir? = null
    var prevDir: Dir? = null
    var lefts = 0
    var rights = 0

    fun changeDir(newDir: Dir) {
        if (prevDir != null) {
            when (newDir) {
                prevDir!!.left -> lefts++
                prevDir!!.right -> rights++
                else -> {}
            }
        } else {
            firstDir = newDir
        }
        prevDir = newDir
    }

    // "Draw" cycle.
    val start = Pair(mapSize/2, mapSize/2)
    visit(start)
    var cur = start

    for ((len, dir) in commands) {
        repeat(len) {
            cur = moveInDir(cur, dir)
            visit(cur)
        }

        changeDir(dir)
    }
    changeDir(firstDir!!)
    assert(cur == start)

    // And now "fill" it.
    fun fill(pos: Pair<Int, Int>) {
        if (!visit(pos)) return

        fill(moveInDir(pos, Dir.UP))
        fill(moveInDir(pos, Dir.DOWN))
        fill(moveInDir(pos, Dir.LEFT))
        fill(moveInDir(pos, Dir.RIGHT))
    }

    val insideDir: (Dir) -> Dir = when (rights - lefts) {
        4 -> {{ it.right }}
        -4 -> {{ it.left }}
        else -> throw IllegalStateException()
    }

    for ((len, dir) in commands) {
        val inside = insideDir(dir)
        repeat(len) {
            cur = moveInDir(cur, dir)
            fill(moveInDir(cur, inside))
        }
    }

    return fill
}
