import Dir.*
import java.util.PriorityQueue

fun main() = test(
    { solve(it, 0, 3) },
    { solve(it, 4, 10) },
)

private data class Arrow(val dst: Pair<Int, Int>, val dir: Dir, val len: Int, val heat: Long, val prev: Arrow?)

private fun solve(input: List<String>, minLen: Int, maxLen: Int): Long {
    val heatMap = StringArray2D(input)

    val marksMap = Array(heatMap.height) { Array(heatMap.width) {
        mutableMapOf<Pair<Dir, Int>, Long>().withDefault { Long.MAX_VALUE }
    }}

    val queue = PriorityQueue<Arrow>(Comparator.comparing { a -> a.heat })

    fun isWorthy(arr: Arrow): Boolean {
        val marks = marksMap[arr.dst]
        val prevHeat = marks.getValue(arr.dir to arr.len)
        return arr.heat < prevHeat
    }

    val initialHeat = 0L
    fun go(arr: Arrow) {
        if (arr.len > maxLen) return
        if (heatMap.getOrNull(arr.dst) == null) return
        if (!isWorthy(arr)) return

        queue += arr
    }

    go(Arrow(0 to 1, RIGHT, 1, initialHeat, null))
    go(Arrow(1 to 0, DOWN, 1, initialHeat, null))

    while (true) {
        val arr = queue.poll()
        if (!isWorthy(arr)) continue

        val newHeat = arr.heat + heatMap[arr.dst].digitToInt()

        if (arr.len >= minLen) {
            marksMap[arr.dst][arr.dir to arr.len] = arr.heat

            if (arr.dst.first == heatMap.height - 1 && arr.dst.second == heatMap.width - 1) {
                return newHeat
            }
        }

        fun turn(newDir: Dir) {
            go(Arrow(moveInDir(arr.dst, newDir), newDir,
                1, newHeat,
                arr))
        }
        if (arr.len >= minLen) {
            turn(arr.dir.left)
            turn(arr.dir.right)
        }

        go(Arrow(moveInDir(arr.dst, arr.dir), arr.dir,
            arr.len + 1, newHeat,
            arr))
    }
}
