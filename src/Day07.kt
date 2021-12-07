import kotlin.math.abs

fun main() {
    fun Int.gaussSum(): Int = this * (this + 1) / 2

    fun part1(input: List<String>): Int {
        val positions = input[0].split(',').map { it.toInt() }
        return (positions.minOrNull()!!..positions.maxOrNull()!!).minOfOrNull { pos -> positions.sumOf { abs(it - pos) } }!!
    }

    fun part2(input: List<String>): Int {
        val positions = input[0].split(',').map { it.toInt() }
        return (positions.minOrNull()!!..positions.maxOrNull()!!).minOfOrNull { pos -> positions.sumOf { abs(it - pos).gaussSum() } }!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
