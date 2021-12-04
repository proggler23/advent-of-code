fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input[0].split(',').map { it.toInt() }
        val boards = Board.parseBoards(input)

        for (number in numbers) {
            for (board in boards) {
                board.check(number)
                if (board.hasWon()) {
                    return number * board.sumOfUnchecked()
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val numbers = input[0].split(',').map { it.toInt() }
        val boards = Board.parseBoards(input)

        for (number in numbers) {
            for (board in boards) {
                board.check(number)
                if (boards.all { it.hasWon() }) {
                    return number * board.sumOfUnchecked()
                }
            }
        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

data class Board(val numbers: List<List<Int>>) {
    private val checked = numbers.map { it.map { false }.toMutableList() }

    fun check(number: Int) {
        numbers.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                if (number == cell) {
                    checked[y][x] = true
                }
            }
        }
    }

    fun hasWon(): Boolean {
        return checked.any { row -> row.all { cell -> cell } } || checked[0].indices.any { col -> checked.all { row -> row[col] } }
    }

    fun sumOfUnchecked(): Int {
        return numbers.mapIndexed { y, row -> row.filterIndexed { x, _ -> !checked[y][x] }.sum() }.sum()
    }

    companion object {
        private val boardSplitRegex = Regex("\\s+")
        fun parseBoards(input: List<String>) = input.drop(1).chunked(6).map { chunk ->
            chunk.drop(1).map { line ->
                line.trim().split(boardSplitRegex).map { it.toInt() }
            }
        }.map { Board(it) }
    }
}
