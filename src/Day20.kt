fun main() {
    fun List<List<Int>>.expand(n: Int, fill: Int = 0) = List(n) { List(this[0].size + 2 * n) { fill } } +
            this.map { line -> List(n) { fill } + line + List(n) { fill } } +
            List(n) { List(this[0].size + 2 * n) { fill } }

    fun List<List<Int>>.collapse(n: Int) = drop(n).dropLast(n).map { it.drop(n).dropLast(n) }

    fun List<List<Int>>.applyAlgorithm(algorithm: List<Int>, n: Int): List<List<Int>> {
        var result = this
        repeat(n) {
            val expanded = result.expand(2, result[0][0])
            val nextImage = expanded.map { it.toMutableList() }
            for (y in 1 until expanded.size - 1) {
                for (x in 1 until expanded[0].size - 1) {
                    val index = buildString {
                        for (dy in -1..1) {
                            for (dx in -1..1) {
                                append(expanded[y + dy][x + dx])
                            }
                        }
                    }.toInt(2)
                    nextImage[y][x] = algorithm[index]
                }
            }
            result = nextImage.collapse(1)
        }
        return result
    }

    fun List<String>.parseImageAndAlgorithm() = Pair(
        this.drop(2).map { line -> line.toCharArray().map { if (it == '#') 1 else 0 } }.expand(1),
        this[0].toCharArray().map { if (it == '#') 1 else 0 }
    )

    fun part1(input: List<String>) = input.parseImageAndAlgorithm().let { (image, algorithm) ->
        image.applyAlgorithm(algorithm, 50).flatten().count { it == 1 }
    }

    fun part2(input: List<String>) = input.parseImageAndAlgorithm().let { (image, algorithm) ->
        image.applyAlgorithm(algorithm, 50).flatten().count { it == 1 }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
