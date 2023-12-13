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
@Suppress("unused")
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

// Inspired by Iterable.sumOf()
inline fun <T> Iterable<T>.productOf(selector: (T) -> Long): Long =
    fold(1) { acc, x -> acc * selector(x) }

fun <T> List<T>.split(separator: T): Sequence<Sequence<T>> = sequence {
    val remaining = this@split.iterator()
    while (remaining.hasNext()) {
        yield(sequence {
            while (remaining.hasNext()) {
                val elem = remaining.next()
                if (elem == separator) {
                    break
                }
                yield(elem)
            }
        })
    }
}

/**
 * The cleaner shorthand for printing output.
 */
@Deprecated("just don't use it")
fun Any?.println() = println(this)

private val PART_NUM_PATTERN = Regex(""".*(?:\b|_)part(\d)(?:\b|_).*""")

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

class StringArray2D(private val strings: List<String>) {

    val height: Int get() = strings.size
    val width: Int get() = strings[0].length

    operator fun get(row: Int, col: Int): Char =
        strings[row][col]

    fun getOrNull(row: Int, col: Int): Char? =
        strings.getOrNull(row)?.getOrNull(col)

    val rows: List<List<Char>> =
        object : AbstractList<List<Char>>() {
            override val size: Int get() = height
            override fun get(index: Int): List<Char> = row(index)
        }

    val cols: List<List<Char>> =
        object : AbstractList<List<Char>>() {
            override val size: Int get() = width
            override fun get(index: Int): List<Char> = col(index)
        }

    fun row(row: Int): List<Char> =
        object : AbstractList<Char>() {
            override val size: Int get() = width
            override fun get(index: Int): Char = get(row, index)
        }

    fun col(col: Int): List<Char> =
        object : AbstractList<Char>() {
            override val size: Int get() = height
            override fun get(index: Int): Char = get(index, col)
        }
}

