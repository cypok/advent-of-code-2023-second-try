import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTimedValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

// Inspired by Iterable.sumOf()
inline fun <T> Iterable<T>.productOf(selector: (T) -> Long): Long =
    fold(1) { acc, x -> acc * selector(x) }

/**
 * The cleaner shorthand for printing output.
 */
@Deprecated("just don't use it")
fun Any?.println() = println(this)

private val PART_NUM_PATTERN = Regex(""".*(?:\b|_)part([12])(?:\b|_).*""")

fun test(vararg parts: (List<String>) -> Any) {
    val className = Throwable().stackTrace
        .map { it.className }
        .dropWhile { !it.startsWith("Day") || !it.endsWith("Kt") }
        .first()
    val day = className.substringBefore("Kt")

    val inputFiles =
        File("src/").listFiles { _, name -> name.startsWith(day) && name.endsWith(".txt") } ?: arrayOf()

    for ((i, p) in parts.withIndex()) {
        for (f in inputFiles.sortedBy { it.length() }) {
            val partNum = i + 1
            val kind = f.nameWithoutExtension
                .substringAfter(day).substringAfter('_')
                .takeIf { it.isNotEmpty() } ?: "real"

            val partNumMatch = PART_NUM_PATTERN.matchEntire(kind)
            if (partNumMatch != null && partNumMatch.groupValues[1].toInt() != partNum) {
                // Skip inputs for other parts.
                continue
            }

            runCatching { f.toPath().readLines() }
                .onSuccess { input ->
                    print("part$partNum, $kind: ")
                    val (result, time) = measureTimedValue { runCatching { p(input) } }
                    print("${result.getOrElse { "ERROR" }} (took ${time.inWholeMilliseconds} ms)")
                    result
                        .onSuccess { println() }
                        .onFailure { it.printStackTrace(System.out) }
                }
        }
    }
}
