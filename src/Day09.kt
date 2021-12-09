fun main() {
    fun part1(input: List<String>): Int {
        val heightMap = input.map { it.toCharArray().map { c -> c.digitToInt() } }
        return heightMap.mapIndexed { row, heights ->
            heights.mapIndexed { col, height ->
                if (isLowPoint(col, row, heightMap)) height + 1 else 0
            }.sum()
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val heightMap = input.map { it.toCharArray().map { c -> c.digitToInt() }.toMutableList() }
        val basins = heightMap.flatMapIndexed { row, heights ->
            heights.mapIndexedNotNull { col, _ ->
                if (isLowPoint(col, row, heightMap))
                    mutableListOf<Int>().also { resolveBasin(col, row, heightMap, it) }
                else null
            }
        }
        return basins.sortedByDescending { it.size }.take(3).fold(1) { result, basin -> result * basin.size }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

operator fun List<List<Int>>.get(x: Int, y: Int) = getOrNull(y)?.getOrNull(x) ?: 9

fun isLowPoint(x: Int, y: Int, heightMap: List<List<Int>>): Boolean {
    val height = heightMap[x, y]
    return heightMap[x - 1, y] > height &&
            heightMap[x + 1, y] > height &&
            heightMap[x, y - 1] > height &&
            heightMap[x, y + 1] > height
}

fun resolveBasin(x: Int, y: Int, heightMap: List<MutableList<Int>>, basin: MutableList<Int>) {
    val height = heightMap[x, y]
    if (height != 9) {
        basin += height
        heightMap[y][x] = 9
        resolveBasin(x - 1, y, heightMap, basin)
        resolveBasin(x + 1, y, heightMap, basin)
        resolveBasin(x, y - 1, heightMap, basin)
        resolveBasin(x, y + 1, heightMap, basin)
    }
}
