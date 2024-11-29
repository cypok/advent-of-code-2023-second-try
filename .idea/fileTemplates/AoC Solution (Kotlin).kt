#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
import utils.*

#set($YearUrl = $PACKAGE_NAME.substring(4))
#set($DayUrl = $Day)
#if ($DayUrl.startsWith("0"))
#set($DayUrl = $DayUrl.substring(1))
#end
// Task description:
//   https://adventofcode.com/$YearUrl/day/$DayUrl

fun main() = test(
    ::solve,
)

private fun solve(input: List<String>): Long =
    0