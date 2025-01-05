package year2019

class IntCodeComputer(program: List<Int>) {
    private val mem = program.toMutableList()

    operator fun get(i: Int) = mem[i]
    operator fun set(i: Int, value: Int) { mem[i] = value }

    fun interpret(input: () -> Int, output: (Int) -> Unit) {
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

                3 -> result(input())
                4 -> output(param())

                5 -> jumpIf(param() != 0)
                6 -> jumpIf(param() == 0)

                7 -> cmp(param() < param())
                8 -> cmp(param() == param())

                else -> error(op)
            }
        }
    }

    fun interpret(input: List<Int>): List<Int> =
        buildList {
            interpret(input.iterator()::next, ::add)
        }

    fun interpret(vararg input: Int): List<Int> =
        interpret(input.asList())

}