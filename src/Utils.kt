import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

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
    val className = Throwable().stackTrace[1].className
    check(className.startsWith("Day"))
    check(className.endsWith("Kt"))

    val day = className.substringBefore("Kt")

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val realInput = readInput(day)

    for ((i, p) in parts.withIndex()) {
        for ((kind, input) in listOf(Pair("test", testInput), Pair("real", realInput))) {
            print("part${i + 1}, $kind: ")
            runCatching { p(input) }
                .onSuccess { println(it) }
                .onFailure { it.printStackTrace(System.out) }
        }
    }
}
