fun main() {
    testCartesianProduct()
}

private fun testCartesianProduct() {
    check(
        listOf(listOf(1, 3, 4), listOf(1, 3, 5), listOf(2, 3, 4), listOf(2, 3, 5)) ==
        listOf(listOf(1, 2), listOf(3), listOf(4, 5)).cartesianProduct())
}