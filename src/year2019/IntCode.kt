package year2019

import utils.*

class IntCodeComputer(program: List<Int>) {
    private val mem = program.toMutableList()

    operator fun get(i: Int) = mem[i]
    operator fun set(i: Int, value: Int) { mem[i] = value }

    fun interpret() {
        var ip = 0
        while (true) {
            val op = mem[ip++]
            when (op) {
                99 -> break
                1, 2 -> {
                    val src1 = mem[mem[ip++]]
                    val src2 = mem[mem[ip++]]
                    val dstAddr = mem[ip++]
                    mem[dstAddr] = when (op) {
                        1 -> src1 + src2
                        2 -> src1 * src2
                        else -> shouldNotReachHere()
                    }
                }
            }
        }
    }

}