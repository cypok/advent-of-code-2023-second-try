enum class Dir {
    UP, DOWN, LEFT, RIGHT;

    val opposite
        get() = when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }

    val left
        get() = when (this) {
            UP -> LEFT
            DOWN -> RIGHT
            LEFT -> DOWN
            RIGHT -> UP
        }

    val right
        get() = left.opposite

}

fun moveInDir(pos: Pair<Int, Int>, dir: Dir) =
    moveInDir(pos.first, pos.second, dir)

fun moveInDir(row: Int, col: Int, dir: Dir) =
    when (dir) {
        Dir.UP -> Pair(row - 1, col)
        Dir.DOWN -> Pair(row + 1, col)
        Dir.LEFT -> Pair(row, col - 1)
        Dir.RIGHT -> Pair(row, col + 1)
    }
