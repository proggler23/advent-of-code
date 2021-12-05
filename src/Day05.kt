import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val lines = input.map { Line.parseLine(it) }
        val field = Field(lines)
        return field.occupied.flatten().count { it > 1 }
    }

    fun part2(input: List<String>): Int {
        val lines = input.map { Line.parseLine(it) }
        val field = Field(lines, diagonals = true)
        return field.occupied.flatten().count { it > 1 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class Field(val lines: List<Line>, val diagonals: Boolean = false) {
    val occupied: List<MutableList<Int>>

    init {
        val maxX = lines.maxOf { max(it.x1, it.x2) }
        val maxY = lines.maxOf { max(it.y1, it.y2) }
        occupied = (0..maxY).map { (0..maxX).map { 0 }.toMutableList() }
        for (line in lines) {
            for ((x, y) in line.getPoints(diagonals)) {
                occupied[y][x] += 1
            }
        }
    }

    override fun toString() = occupied.joinToString("\n") { row -> row.joinToString(" ") }
}

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    private val minX = min(x1, x2)
    private val maxX = max(x1, x2)
    private val minY = min(y1, y2)
    private val maxY = max(y1, y2)

    fun getPoints(diagonals: Boolean) =
        if (x1 == x2) {
            (minY..maxY).map { x1 to it }
        } else if (y1 == y2) {
            (minX..maxX).map { it to y1 }
        } else if (diagonals && abs(x1 - x2) == abs(y1 - y2)) {
            val invert = ((x1 - x2) < 0) != ((y1 - y2 < 0))
            val fY = if (invert) -1 else 1
            val startY = if (invert) maxY else minY
            (0..maxX - minX).map { minX + it to startY + it * fY }
        } else {
            emptyList()
        }

    override fun toString() = "$minX,$minY -> $maxX,$maxY"

    companion object {
        fun parseLine(line: String): Line {
            val (pt1, pt2) = line.split("->").map { points -> points.trim().split(',').map { it.toInt() } }
            return Line(pt1[0], pt1[1], pt2[0], pt2[1])
        }
    }
}
