package year2024

import utils.*

// Task description:
//   https://adventofcode.com/2024/day/19

fun main() = runAoc {
    example {
        answer1(6)
        answer2(16)
        """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
        """
    }
    solution {
        val (availableTowelsStr, designs) = lines.splitByEmptyLines().toList()
        val availableTowels = availableTowelsStr.single().split(", ").toList()

        fun isPossible(design: String, offset: Int): Boolean =
            (offset == design.length) ||
                    availableTowels.any { towel ->
                        design.startsWith(towel, offset) && isPossible(design, offset + towel.length)
                    }

        fun countWays(design: String, offset: Int): Long {
            if (offset == design.length) {
                return 1
            }

            return availableTowels.sumOf { towel ->
                if (!design.startsWith(towel, offset)) return@sumOf 0L
                countWays(design, offset + towel.length)
            }
        }

        if (isPart1) {
            designs.count { isPossible(it, 0) }
        } else {
            designs.sumOf { countWays(it, 0) }
        }
    }
}