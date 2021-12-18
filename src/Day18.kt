fun main() {
    fun parse(input: String) = buildList {
        var depth = 0
        for (ch in input) {
            if (ch == '[') {
                depth++
            } else if (ch == ']') {
                depth--
            } else if (ch == ',') {
                continue
            } else {
                add(ch.digitToInt() to depth)
            }
        }
    }

    fun List<Pair<Int, Int>>.step(): Pair<List<Pair<Int, Int>>, Boolean> {
        forEachIndexed { index, (_, depth) ->
            if (depth - 1 == 4) {
                val result = this.toMutableList()
                if (index > 0) {
                    result[index - 1] = this[index - 1].copy(first = this[index - 1].first + this[index].first)
                }
                if (index < size - 2) {
                    result[index + 2] = this[index + 2].copy(first = this[index + 2].first + this[index + 1].first)
                }
                result.removeAt(index + 1)
                result[index] = 0 to depth - 1
                return result to false
            }
        }
        forEachIndexed { index, (value, depth) ->
            if (value > 9) {
                val result = this.toMutableList()
                result.add(index, value / 2 to depth + 1)
                result[index + 1] = (value + 1) / 2 to depth + 1
                return result to false
            }
        }
        return this to true
    }

    fun List<Pair<Int, Int>>.reduce(): List<Pair<Int, Int>> {
        var number = this
        while (true) {
            val (result, done) = number.step()
            if (done) {
                return result
            } else {
                number = result
            }
        }
    }

    fun List<Pair<Int, Int>>.add(other: List<Pair<Int, Int>>) = (this + other).map { (v, d) -> v to d + 1 }.reduce()

    fun List<Pair<Int, Int>>.score(): Int {
        val number = toMutableList()
        for (depth in 4 downTo 1) {
            number.forEachIndexed { index, (_, d) ->
                if (d == depth) {
                    number[index] = number[index].first * 3 + number[index + 1].first * 2 to depth - 1
                    number[index + 1] = 0 to -1
                }
            }
            number.removeIf { (_, d) -> d == -1 }
        }
        return number.first().first
    }

    fun part1(input: List<String>): Int {
        val number = input.map { parse(it) }.reduce { left, right -> left.add(right) }
        return number.score()
    }

    fun part2(input: List<String>): Int {
        val numbers = input.map { parse(it) }
        return numbers.indices.flatMap { i -> numbers.indices.mapNotNull { j -> if (i == j) null else numbers[i] to numbers[j] } }
            .maxOf { (left, right) -> left.add(right).score() }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}

