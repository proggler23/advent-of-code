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

data class BoardField(val number: Int, var checked: Boolean = false)

data class Board(val fields: List<List<BoardField>>) {
    fun check(number: Int) {
        fields.flatten().filter { it.number == number }.forEach { it.checked = true }
    }

    fun hasWon(): Boolean {
        return fields.any { row -> row.all { cell -> cell.checked } } || fields[0].indices.any { col -> fields.all { row -> row[col].checked } }
    }

    fun sumOfUnchecked(): Int {
        return fields.flatten().filter { !it.checked }.sumOf { it.number }
    }

    companion object {
        private val boardSplitRegex = Regex("\\s+")
        fun parseBoards(input: List<String>) = input.drop(1).chunked(6).map { chunk ->
            chunk.drop(1).map { line ->
                line.trim().split(boardSplitRegex).map { it.toInt() }
            }
        }.map { numbers -> Board(numbers.map { row -> row.map { BoardField(number = it) } }) }
    }
}
