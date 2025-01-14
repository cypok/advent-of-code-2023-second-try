package year2019

import utils.*

// Task description:
//   https://adventofcode.com/2019/day/13

@Suppress("LocalVariableName")
fun main() = runAoc {
    solution {
        val EMPTY = ' '
        val WALL = FULL_BLOCK
        val BLOCK = '#'
        val PADDLE = '='
        val BALL = 'o'
        val cellsById = arrayOf(EMPTY, WALL, BLOCK, PADDLE, BALL)

        val size = 100
        val canvas = Array(size) { Array(size) { EMPTY } }

        val game = IntCodeComputer(intCode)

        if (isPart1) {
            for ((x, y, id) in game.run().map { it.toIntExact() }.chunked(3)) {
                canvas[y, x] = cellsById[id]
            }

            printExtra(canvas.toAsciiArt(EMPTY))
            canvas.valuesIndexed.count { (ch, _) -> ch == BLOCK }
        } else {
            game[0] = 2

        }
    }
}
