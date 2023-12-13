fun main() = test(
    { solve(it, 0) },
    { solve(it, 1) },
)

private fun solve(input: List<String>, expectedSmudgesCount: Int): Long {
    return input.split("").sumOf { map ->
        when (val ref = solveOne(StringArray2D(map.toList()), expectedSmudgesCount)) {
            is Reflection.Vertical ->ref.col
            is Reflection.Horizontal -> ref.row * 100
        }.toLong()
    }
}

private sealed class Reflection {
    data class Vertical(val col: Int) : Reflection()
    data class Horizontal(val row: Int) : Reflection()
}

private fun solveOne(map: StringArray2D, expectedSmudgesCount: Int): Reflection {
    fun tryDimension(width: Int, rows: List<List<Char>>): Int? =
        (1 until width).firstOrNull { colNum ->
            rows.sumOf { row -> countSmudges(row, colNum) } == expectedSmudgesCount
        }

    return tryDimension(map.width, map.rows)?.let { Reflection.Vertical(it) } ?:
           tryDimension(map.height, map.cols)?.let { Reflection.Horizontal(it) } ?:
           throw IllegalArgumentException()
}

private fun countSmudges(row: List<Char>, colNum: Int): Int {
    var l = colNum - 1
    var r = colNum
    var smudges = 0
    while (0 <= l && r < row.size) {
        if (row[l] != row[r]) {
            // We might be able to check smudges overflow earlier, but it isn't required.
            smudges++
        }
        l--
        r++
    }
    return smudges
}
