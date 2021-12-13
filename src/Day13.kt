import kotlin.math.abs

fun main() {
    val foldAlongX = "fold along x="

    fun DotGrid.fold(input: String) = input.substring(foldAlongX.length).toInt().let { line ->
        if (input.startsWith(foldAlongX)) foldVertically(line) else foldHorizontally(line)
    }

    fun part1(input: List<String>): Int {
        val pts = input.takeWhile { it.isNotEmpty() }.map { it.split(',').let { (x, y) -> x.toInt() to y.toInt() } }
        val dotGrid = DotGrid(pts.toSet()).fold(input.drop(pts.size + 1)[0])
        return dotGrid.pts.size
    }

    fun part2(input: List<String>): DotGrid {
        val pts = input.takeWhile { it.isNotEmpty() }.map { it.split(',').let { (x, y) -> x.toInt() to y.toInt() } }
        return input.drop(pts.size + 1).fold(DotGrid(pts.toSet())) { result, line -> result.fold(line) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

data class DotGrid(val pts: Set<Pair<Int, Int>>) {
    fun foldHorizontally(line: Int) = DotGrid(pts.map { (x, y) -> x to (line - abs(line - y)) }.toSet())
    fun foldVertically(line: Int) = DotGrid(pts.map { (x, y) -> (line - abs(line - x)) to y }.toSet())

    override fun toString() = buildString {
        for (y in 0..pts.maxOf { (_, y) -> y }) {
            for (x in 0..pts.maxOf { (x) -> x }) {
                if (x to y in pts) {
                    append("# ")
                } else {
                    append(". ")
                }
            }
            appendLine()
        }
    }
}
