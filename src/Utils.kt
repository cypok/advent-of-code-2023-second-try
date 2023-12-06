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

/**
 * The cleaner shorthand for printing output.
 */
@Deprecated("just don't use it")
fun Any?.println() = println(this)

fun test(vararg parts: (List<String>) -> Long) {
    val className = Throwable().stackTrace
        .map { it.className }
        .dropWhile { !it.startsWith("Day") || !it.endsWith("Kt") }
        .first()
    val day = className.substringBefore("Kt")

    for ((i, p) in parts.withIndex()) {
        for ((kind, inputSuffix) in listOf(Pair("test", "_test"), Pair("real", ""))) {
            runCatching { readInput("$day$inputSuffix") }
                .onSuccess { input ->
                    print("part${i + 1}, $kind: ")
                    val (result, time) = measureTimedValue { runCatching { p(input) } }
                    print("${result.getOrElse { "ERROR" }} (took ${time.inWholeMilliseconds} ms)")
                    result
                        .onSuccess { println() }
                        .onFailure { it.printStackTrace(System.out) }
                }
        }
    }
}
