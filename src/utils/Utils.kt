package utils

import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URI
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.math.max
import kotlin.math.min
import kotlin.streams.asSequence
import kotlin.time.measureTimedValue

/**
 * Converts string to md5 hash.
 */
@Suppress("unused")
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

// Inspired by Iterable.sumOf()
inline fun <T> Iterable<T>.productOf(selector: (T) -> Long): Long =
    fold(1) { acc, x -> acc * selector(x) }

fun <T> List<T>.split(separator: T): Sequence<List<T>> = sequence {
    val remaining = this@split.iterator()
    while (remaining.hasNext()) {
        yield(buildList {
            while (remaining.hasNext()) {
                val elem = remaining.next()
                if (elem == separator) {
                    break
                }
                add(elem)
            }
        })
    }
}

fun List<String>.splitByEmptyLines(): Sequence<List<String>> =
    split("")

fun String.words(): List<String> =
    split("""\s+""".toRegex())

fun String.numbers(): List<Long> =
    split("""[^0-9+-]+""".toRegex()).filterNot { it.isEmpty() }.map { it.toLong() }

fun String.numbersAsInts(): List<Int> =
    numbers().map { it.toIntExact() }

private val MAIN_CLASS_PATTERN = Regex(""".*Day\d+(?:Kt)?""")
private val PART_NUM_PATTERN = Regex(""".*(?:\b|_)part(\d)(?:\b|_).*""")

fun test(vararg parts: (List<String>) -> Any) {
    // className = year2023.Day10
    val className = StackWalker.getInstance().walk { frames ->
        frames.asSequence()
            .map { it.className }
            .firstOrNull { MAIN_CLASS_PATTERN.matches(it) }
            ?.substringBefore("Kt")
            ?: throw IllegalCallerException("this function should be called from DayNN class")
    }
    // year = year2023
    val year = className.substringBeforeLast(".Day")
    // day = Day10
    val day = "Day" + className.substringAfterLast("Day")

    val inputsDir = "src/${year.replace('.', '/')}"
    val inputFiles =
        File(inputsDir)
            .listFiles { _, name ->
                name.startsWith(day) && name.endsWith(".txt") }
            ?: emptyArray()

    val inputs =
        inputFiles.sortedBy { it.length() }
            .map { f ->
                val kind = f.nameWithoutExtension
                    .substringAfter(day).substringAfter('_')
                    .takeIf { it.isNotEmpty() } ?: "real"
                kind to f
            }
            .toMutableList()

    if (!inputs.any { it.first == "real" }) {
        inputs += "real" to downloadRealInput(
            year.substringAfter("year").toInt(),
            day.substringAfter("Day").toInt(),
            "$inputsDir/$day.txt"
        )
    }

    for ((i, p) in parts.withIndex()) {
        val partNum = i + 1
        for ((kind, f) in inputs) {
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

private fun downloadRealInput(year: Int, day: Int, outputPath: String): File {
    val url = URI("https://adventofcode.com/$year/day/$day/input").toURL()
    try {
        val connection = url.openConnection() as HttpURLConnection
        val sessionCookie = Path(".session-cookie").readText().trim()
        connection.setRequestProperty("Cookie", "session=$sessionCookie")
        connection.setRequestProperty("User-Agent", "github.com/cypok/advent-of-code by @cypok")

        when (connection.responseCode) {
            HttpURLConnection.HTTP_OK ->
                connection.inputStream.use { input ->
                    FileOutputStream(outputPath).use { output ->
                        output.write(input.readAllBytes())
                    }
                    return File(outputPath)
                }
            else -> throw RuntimeException("Bad response: ${connection.responseCode}, ${connection.responseMessage}")
        }
    } catch (e: Exception) {
        throw RuntimeException("Cannot download $url", e)
    }
}

/**
 * Transform
 *
 *     [[a,b], [c], [d,e]]
 *
 * to
 *
 *     [[a,c,d], [a,c,e], [b,c,d], [b,c,e]]
 */
fun <T> Collection<Iterable<T>>.cartesianProduct(): List<List<T>> {
    if (isEmpty()) return listOf(emptyList())
    val tails = drop(1).cartesianProduct()
    return first().flatMap { head -> tails.map { tail -> listOf(head) + tail } }
}

fun <T> Collection<T>.cycle(): Sequence<T> =
    generateSequence { this }.flatten()

fun gcd(x: Long, y: Long): Long {
    var a = max(x, y)
    var b = min(x, y)
    while (b > 0L) {
        val rem = a % b
        a = b
        b = rem
    }
    return a
}

fun lcm(x: Long, y: Long) = x / gcd(x, y) * y

fun Long.toIntExact() = Math.toIntExact(this)

inline fun <T, R> Pair<T, T>.map(transform: (T) -> R): Pair<R, R> =
    Pair(transform(first), transform(second))

operator fun <T> List<T>.component6() = get(5)

fun <T> List<T>.middle(): T = this[this.size/2]
