import Module.*

fun main() = test(
    ::solve1,
    ::solve2,
)

private sealed class Module(val name: String, val outputNames: List<String>) {
    abstract fun process(inputs: List<Signal>): List<Pulse>

    open fun prepareInput(input: String): Unit {
    }

    class Broadcaster(outputs: List<String>) : Module(NAME, outputs) {
        companion object {
            const val NAME = "broadcaster"
        }

        override fun process(inputs: List<Signal>) = listOf(inputs.single().value)
    }

    class FlipFlop(name: String, outputs: List<String>) : Module(name, outputs) {
        var state = false

        override fun process(inputs: List<Signal>) =
            if (inputs.all { it.value }) {
                emptyList()
            } else {
                state = !state
                listOf(state)
            }
    }

    class Conjunction(name: String, outputs: List<String>) : Module(name, outputs) {
        var state = mutableMapOf<String, Pulse>()

        override fun process(inputs: List<Signal>): List<Pulse> =
            inputs.map {
                state += it.src to it.value
                !state.values.all { it }
            }

        override fun prepareInput(input: String): Unit {
            state[input] = false
        }
    }
}

private typealias Pulse = Boolean

private data class Signal(val src: String, val value: Pulse, val dst: String) {
    override fun toString(): String =
        "$src -${if (value) "high" else "low"}-> $dst"
}

private fun parseModules(input: List<String>): Map<String, Module> {
    val modules = input.associate {
        val fullName = it.substringBefore(" -> ")
        val outputs = it.substringAfter(" -> ").split(", ")
        val module = when {
            fullName == Broadcaster.NAME -> Broadcaster(outputs)
            fullName.startsWith('%') -> FlipFlop(fullName.substring(1), outputs)
            fullName.startsWith('&') -> Conjunction(fullName.substring(1), outputs)
            else -> throw IllegalArgumentException(it)
        }
        module.name to module
    }

    for ((_, src) in modules) {
        for (dst in src.outputNames) {
            modules[dst]?.prepareInput(src.name)
        }
    }

    return modules
}

private fun sendSignals(modules: Map<String, Module>, signals: List<Signal>): List<Signal> =
    signals.groupBy { it.dst }.flatMap { (dst, ss) ->
        val module = modules[dst] ?: return@flatMap emptyList()
        val newPulses = module.process(ss) ?: return@flatMap emptyList()
        newPulses.flatMap { newPulse ->
            module.outputNames.map { Signal(dst, newPulse, it) }
        }
    }

private fun pressButtonAndWait(modules: Map<String, Module>): Triple<Long, Long, Boolean> {
    var lows = 0L
    var highs = 0L
    var rxGotLow = false

    var signals = listOf(Signal("button", false, Broadcaster.NAME))
    while (signals.isNotEmpty()) {
        lows += signals.count { !it.value }
        highs += signals.count { it.value }
        rxGotLow = rxGotLow || (signals.any { it.dst == "rx" && !it.value })
        signals = sendSignals(modules, signals)
    }

    return Triple(lows, highs, rxGotLow)
}

private fun solve1(input: List<String>): Long {
    val modules = parseModules(input)

    var totalLow = 0L
    var totalHigh = 0L
    repeat(1000) {
        val (lows, highs, _) = pressButtonAndWait(modules)
        totalLow += lows
        totalHigh += highs
    }

    return totalLow * totalHigh
}

private fun solve2(input: List<String>): Long {
    val modules = parseModules(input)

    var times = 0L
    while (true) {
        times++
        val (_, _, finish) = pressButtonAndWait(modules)
        if (finish) {
            return times
        }
    }
}
