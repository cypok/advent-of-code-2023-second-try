package utils

class CyclicState<T>(initial: T) {
    var current = initial

    private var previous = mutableListOf<T>()

    fun tick(time: Long) {
        check(previous.size.toLong() == time)
        previous += current
    }

    fun detectCycle(): Int? =
        detectCycle(previous)

    fun extrapolateUntil(futureTime: Long): T {
        val cycle = detectCycle()
        check(cycle != null)
        val lastTime = previous.size.toLong()
        return previous[
            previous.size - 1
                    - cycle
                    + ((futureTime - lastTime) % cycle).toIntExact()]
    }
}

fun <T> Collection<CyclicState<T>>.tickAll(time: Long): Unit =
    forEach { it.tick(time) }

fun <T> Collection<CyclicState<T>>.detectCommonCycle(): Long? =
    fold(1) { c, s ->
        if (c == null) {
            null
        } else {
            s.detectCycle()?.let { lcm(it.toLong(), c) }
        }
    }


fun <T> detectCycle(values: List<T>, skipInit: Int = values.size/10): Int? {
    fun getLast(idxFromEnd: Int) =
        values[values.size - 1 - idxFromEnd]

    fun isCycle(len: Int): Boolean {
        for (i in len until values.size - skipInit) {
            if (getLast(i) != getLast(i % len)) {
                return false
            }
        }
        return true
    }

    return (1 until values.size / 2)
        .firstOrNull { isCycle(it) }
}

