fun main() {
    fun String.isBigCave() = all { it.isUpperCase() }
    fun String.isSmallCave() = !isBigCave()

    fun List<String>.containsSmallCaveMoreThanOnce() = filter { it.isSmallCave() }
        .let { smallCaves -> smallCaves.toSet().size != smallCaves.size }

    val canVisit1: (String, List<String>) -> Boolean = { cave, visited ->
        cave.isBigCave() || cave !in visited
    }
    val canVisit2: (String, List<String>) -> Boolean = { cave, visited ->
        canVisit1(cave, visited) || !visited.containsSmallCaveMoreThanOnce()
    }

    fun Map<String, List<String>>.findPaths(
        from: String,
        to: String,
        visited: List<String>,
        canVisit: (String, List<String>) -> Boolean
    ): List<List<String>> =
        if (from == to) {
            listOf(visited)
        } else {
            this[from].orEmpty()
                .filter { it != "start" && canVisit(it, visited) }
                .flatMap { findPaths(it, to, visited + it, canVisit) }
        }

    fun parseInput(input: List<String>) = buildMap<String, List<String>> {
        for ((from, to) in input.map { it.split('-') }) {
            compute(from) { _, v -> v.orEmpty() + to }
            compute(to) { _, v -> v.orEmpty() + from }
        }
    }


    fun part1(input: List<String>) = parseInput(input).findPaths("start", "end", listOf("start"), canVisit1).size
    fun part2(input: List<String>) = parseInput(input).findPaths("start", "end", listOf("start"), canVisit2).size

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 226)
    check(part2(testInput) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
