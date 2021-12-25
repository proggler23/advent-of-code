const val EAST_FACING = true
const val SOUTH_FACING = false
val BLANK = null

fun main() {
    operator fun List<List<Boolean?>>.get(x: Int, y: Int) =
        this[(y + this.size) % this.size][(x + this[0].size) % this[0].size]

    fun List<List<Boolean?>>.moveEastFacing() = indices.map { y ->
        this[y].indices.map { x ->
            when (val spot = this[y][x]) {
                BLANK -> if (this[x - 1, y] == EAST_FACING) EAST_FACING else BLANK
                EAST_FACING -> if (this[x + 1, y] == BLANK) BLANK else EAST_FACING
                else -> spot
            }
        }
    }

    fun List<List<Boolean?>>.moveSouthFacing() = indices.map { y ->
        this[y].indices.map { x ->
            when (val spot = this[y][x]) {
                BLANK -> if (this[x, y - 1] == SOUTH_FACING) SOUTH_FACING else BLANK
                SOUTH_FACING -> if (this[x, y + 1] == BLANK) BLANK else SOUTH_FACING
                else -> spot
            }
        }
    }

    fun part1(input: List<String>): Int {
        var herd = input.map { line -> line.map { if (it == '.') null else it == '>' } }
        var lastHerd: List<List<Boolean?>>
        var i = 0
        do {
            lastHerd = herd
            herd = herd.moveEastFacing().moveSouthFacing()
            i++
        } while(herd != lastHerd)
        return i
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 58)

    val input = readInput("Day25")
    println(part1(input))
}
