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

    @Test
    fun testStringArray2DWide() {
        val map = StringArray2D( """
            abcd
            efgh
        """.trimIndent().lines())
        assertEquals(listOf("abcd", "efgh"), map.rows.map { it.asString() })
        assertEquals(listOf("ae", "bf", "cg", "dh"), map.cols.map { it.asString() })

        assertEquals(listOf("e", "af", "bg", "ch", "d"), map.diagonalsRight.map { it.asString() })
        assertEquals(listOf("a", "be", "cf", "dg", "h"), map.diagonalsLeft.map { it.asString() })
    }

    @Test
    fun testStringArray2DNarrow() {
        val map = StringArray2D("""
            ae
            bf
            cg
            dh
        """.trimIndent().lines())
        assertEquals(listOf("d", "ch", "bg", "af", "e"), map.diagonalsRight.map { it.asString() })
        assertEquals(listOf("a", "eb", "fc", "gd", "h"), map.diagonalsLeft.map { it.asString() })
    }

    @Test
    fun testStringArray2DDiagonalsBig() {
        val map = StringArray2D("""
            abcd
            efgh
            ijkl
        """.trimIndent().lines())
        assertEquals(listOf("i", "ej", "afk", "bgl", "ch", "d"), map.diagonalsRight.map { it.asString() })
        assertEquals(listOf("a", "be", "cfi", "dgj", "hk", "l"), map.diagonalsLeft.map { it.asString() })
    }

}