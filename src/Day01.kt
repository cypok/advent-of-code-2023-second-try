fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (fst, lst) = line.fold(Pair(-1, -1)) { acc, ch ->
                if (ch.isDigit()) {
                    var (fst, _) = acc
                    val lst = ch.digitToInt()
                    if (fst == -1) {
                        fst = lst
                    }
                    Pair(fst, lst)
                } else {
                    acc
                }
            }
            fst * 10 + lst
        }
    }

    fun part2(input: List<String>): Int {
        fun digitToInt(digit: String): Int {
            return if (digit.length == 1) {
                digit[0].digitToInt()
            } else {
                when (digit) {
                    "one" -> 1
                    "two" -> 2
                    "three" -> 3
                    "four" -> 4
                    "five" -> 5
                    "six" -> 6
                    "seven" -> 7
                    "eight" -> 8
                    "nine" -> 9
                    else -> throw AssertionError(digit)
                }
            }
        }

        fun findDigit(regex: Regex, line: String): Int {
            val m = regex.matchAt(line, 0)
            check(m != null)
            return digitToInt(m.groups[1]!!.value)
        }

        return input.sumOf { line ->
            val digit = "([0-9]|one|two|three|four|five|six|seven|eight|nine)"
            val fstRegex = Regex(".*?$digit")
            val lstRegex = Regex(".*$digit")
            10 * findDigit(fstRegex, line) + findDigit(lstRegex, line)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day01_test_part1")
    println(part1(testInput1))
    val testInput2 = readInput("Day01_test_part2")
    println(part2(testInput2))

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
