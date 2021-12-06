fun main() {
    fun parseFishes(input: List<String>): MutableMap<Int, Long> = input[0].split(',').map { it.toInt() }
        .groupingBy { it }.eachCount().mapValues { (_, v) -> v.toLong() }
        .toMutableMap()

    fun MutableMap<Int, Long>.iterate(): MutableMap<Int, Long> {
        val result = mapKeysTo(mutableMapOf()) { (k) -> k - 1 }
        val birthable = result.remove(-1) ?: 0L
        result[6] = birthable + (result[6] ?: 0L)
        result[8] = birthable
        return result
    }

    fun part1(input: List<String>): Long {
        var fishes = parseFishes(input)
        repeat(80) { fishes = fishes.iterate() }
        return fishes.values.sum()
    }

    fun part2(input: List<String>): Long {
        var fishes = parseFishes(input)
        repeat(256) { fishes = fishes.iterate() }
        return fishes.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
