fun main() {
    fun getCharCountsAfterIterations(input: List<String>, iterations: Int): Map<Char, Long> {
        val template = input[0]
        val insertions = input.drop(2).associate {
            it.split(" -> ").let { (from, to) -> from to ("${from[0]}$to" to "$to${from[1]}") }
        }
        val mappings = buildMap<String, Set<String>> {
            insertions.forEach { (from, to) ->
                compute(to.first) { _, set -> set.orEmpty() + from }
                compute(to.second) { _, set -> set.orEmpty() + from }
            }
        }
        var countsPerDoubleChar = template.windowed(2).groupingBy { it }.eachCount().mapValues { (_, v) -> v.toLong() }
        repeat(iterations) {
            countsPerDoubleChar = insertions.mapValues { (key) ->
                mappings[key].orEmpty().sumOf { countsPerDoubleChar[it] ?: 0 }
            }
        }
        return buildMap<Char, Long> {
            this[template[0]] = 1L
            this[template.last()] = 1L
            for ((key, count) in countsPerDoubleChar) {
                this[key[0]] = (this[key[0]] ?: 0) + count
                this[key[1]] = (this[key[1]] ?: 0) + count
            }
        }.mapValues { (_, v) -> v / 2 }
    }

    fun part1(input: List<String>): Long {
        val counts = getCharCountsAfterIterations(input, 10).values.sorted()
        return counts.last() - counts.first()
    }

    fun part2(input: List<String>): Long {
        val counts = getCharCountsAfterIterations(input, 40).values.sorted()
        return counts.last() - counts.first()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
