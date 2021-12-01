fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toInt() }.zipWithNext().fold(0) { acc, (last, current) ->
            acc + (if (current > last) 1 else 0)
        }
    }

    fun part2(input: List<String>): Int {
        val sums = input.map { it.toInt() }.windowed(3).map { it.sum() }
        return sums.zipWithNext().fold(0) { acc, (last, current) ->
            acc + (if (current > last) 1 else 0)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 1)
    check(part2(testInput) == 0)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
