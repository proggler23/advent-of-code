fun main() {
    fun List<MutableList<Int>>.tryFlash(row: Int, col: Int): Int =
        if (this[row][col] > 9) {
            this[row][col] = Int.MIN_VALUE
            1 + (-1..1).flatMap { dy -> (-1..1).map { dx -> dy to dx } }
                .filter { (dy, dx) -> this.getOrNull(row + dy)?.getOrNull(col + dx) != null }
                .onEach { (dy, dx) -> this[row + dy][col + dx]++ }
                .sumOf { (dy, dx) -> tryFlash(row + dy, col + dx) }
        } else 0

    fun List<MutableList<Int>>.stepAndGetFlashes(): Int {
        var flashes = 0

        this.indices.flatMap { row -> this[row].indices.map { col -> row to col } }
            .onEach { (row, col) -> this[row][col]++ }
            .onEach { (row, col) -> flashes += this.tryFlash(row, col) }
            .onEach { (row, col) ->
                if (this[row][col] < 0) {
                    this[row][col] = 0
                }
            }

        return flashes
    }

    fun part1(input: List<String>): Int {
        val octopi = input.map { it.toCharArray().map { ch -> ch.digitToInt() }.toMutableList() }
        return (0 until 100).sumOf { octopi.stepAndGetFlashes() }
    }

    fun part2(input: List<String>): Int {
        val octopi = input.map { it.toCharArray().map { ch -> ch.digitToInt() }.toMutableList() }
        return generateSequence(1) { it + 1 }.first { octopi.stepAndGetFlashes() == octopi.size * octopi[0].size }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
