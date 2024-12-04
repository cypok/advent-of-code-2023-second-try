package utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Tests {

    @Test
    fun testCartesianProduct() {
        assertEquals(
            listOf(listOf(1, 3, 4), listOf(1, 3, 5), listOf(2, 3, 4), listOf(2, 3, 5)),
            listOf(listOf(1, 2), listOf(3), listOf(4, 5)).cartesianProduct()
        )
    }

    @Test
    fun testGcdLcm() {
        assertEquals(5L, gcd(15, 10))
        assertEquals(5L, gcd(10, 15))
        assertEquals(30L, lcm(15, 10))
        assertEquals(30L, lcm(10, 15))
        assertEquals(1L, gcd(37, 1))
        assertEquals(1L, gcd(1, 37))
        assertEquals(37L, lcm(37, 1))
        assertEquals(37L, lcm(1, 37))
    }

    @Test
    fun testCycle() {
        assertEquals(listOf(1, 2, 3, 1, 2, 3, 1), listOf(1, 2, 3).cycle().take(7).toList())
    }

}