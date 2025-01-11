package year2019

import kotlinx.coroutines.runBlocking
import year2019.IntCodeComputer.State.*

class IntCodeComputer(program: List<Int>) {
    enum class State {
        NOT_STARTED,
        RUNNING,
        WAITING_IO,
        FINISHED,
    }

    @Volatile
    var state = NOT_STARTED
        private set

    private val mem = program.toMutableList()

    operator fun get(i: Int): Int {
        check(state == NOT_STARTED || state == FINISHED)
        return mem[i]
    }
    operator fun set(i: Int, value: Int) {
        check(state == NOT_STARTED || state == FINISHED)
        mem[i] = value
    }

    private fun changeState(from: State, to: State) {
        check(state == from)
        state = to
    }

    private inline fun <R> withState(from: State, to: State, action: () -> R): R {
        changeState(from, to)
        try {
            return action()
        } finally {
            changeState(to, from)
        }
    }

    private inline fun <R> io(action: () -> R) =
        withState(RUNNING, WAITING_IO) { action() }

    suspend fun run(input: suspend () -> Int, output: suspend (Int) -> Unit) {
        changeState(NOT_STARTED, RUNNING)

        var ip = 0
        while (true) {
            val opAndMode = mem[ip++]
            val op = opAndMode % 100

            var remainingModes = opAndMode / 100
            fun param(): Int {
                val mode = remainingModes % 10
                remainingModes /= 10
                val raw = mem[ip++]
                return when (mode) {
                    0 -> mem[raw]
                    1 -> raw
                    else -> error(mode)
                }
            }

            fun result(value: Int) {
                val raw = mem[ip++]
                mem[raw] = value
            }

            fun jumpIf(cond: Boolean) {
                val dst = param()
                if (cond) {
                    ip = dst
                }
            }

            fun cmp(cond: Boolean) =
                result(if (cond) 1 else 0)

            when (op) {
                99 -> break

                1 -> result(param() + param())
                2 -> result(param() * param())

                3 -> result(io { input() })
                4 -> param().let { io { output(it) } }

                5 -> jumpIf(param() != 0)
                6 -> jumpIf(param() == 0)

                7 -> cmp(param() < param())
                8 -> cmp(param() == param())

                else -> error(op)
            }
        }

        changeState(RUNNING, FINISHED)
    }

    fun run(input: List<Int>): List<Int> =
        runBlocking() {
            buildList {
                run(input.iterator()::next, ::add)
            }
        }

    fun run(vararg input: Int): List<Int> =
        run(input.asList())

}