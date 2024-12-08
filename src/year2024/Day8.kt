package year2024

import utils.*

// Task description:
//   https://adventofcode.com/2024/day/8

fun main() = runAoc {
    example {
        answer1(14)
        answer2(34)
        """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
        """
    }

    solution {
        val antennas = buildMap {
            for (i in 0..<map.height) {
                for (j in 0..<map.width) {
                    val ch = map[i, j]
                    if (!ch.isLetterOrDigit()) continue
                    val points = getOrPut(ch) { mutableListOf() }
                    points += i x j
                }
            }
        }

        antennas.values.flatMap { locs ->
            locs.combinations().flatMap { (p1, p2) ->
                antinodesLocations(isPart2, map, p1, p2)
            }
        }.toSet().size
    }
}

private fun antinodesLocations(advanced: Boolean, map: StringArray2D, p1: Point, p2: Point) = buildList {
    for ((start, diff) in listOf(1 to 1, 0 to -1)) {
        val indices =
            if (advanced) generateSequence(start) { it + diff }
            else sequenceOf(start + diff)
        val points = indices
            .map { (it * p1.i - (it - 1) * p2.i) x (it * p1.j - (it - 1) * p2.j) }
            .takeWhile { it in map }
            .toList()
        addAll(points)
    }
}