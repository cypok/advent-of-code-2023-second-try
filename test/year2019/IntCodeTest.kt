package year2019

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import utils.numbersAsInts

class IntCodeTest {

    fun run(program: List<Int>) =
        IntCodeComputer(program)
            .also { it.interpret() }

    fun run(program: String) =
        run(program.numbersAsInts())

    @Test
    fun day02Add() {
        assertEquals(2, run("1,0,0,0,99")[0])
    }

    @Test
    fun day02Mul() {
        assertEquals(6, run("2,3,0,3,99")[3])
    }

    @Test
    fun day02MulFar() {
        assertEquals(99 * 99, run("2,4,4,5,99,0")[5])
    }

    @Test
    fun day02AddAndMul() {
        assertEquals(30, run("1,1,1,4,99,5,6,0,99")[0])
    }

}