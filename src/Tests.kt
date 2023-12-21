fun main() {
    testCartesianProduct()
    testGcdLcm()
}

private fun testCartesianProduct() {
    check(
        listOf(listOf(1, 3, 4), listOf(1, 3, 5), listOf(2, 3, 4), listOf(2, 3, 5)) ==
        listOf(listOf(1, 2), listOf(3), listOf(4, 5)).cartesianProduct())
}

private fun testGcdLcm() {
    check(5L == gcd(15, 10))
    check(5L == gcd(10, 15))
    check(30L == lcm(15, 10))
    check(30L == lcm(10, 15))
    check(1L == gcd(37, 1))
    check(1L == gcd(1, 37))
    check(37L == lcm(37, 1))
    check(37L == lcm(1, 37))
}
