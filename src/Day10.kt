import java.util.*

fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf {
            val stack = Stack<Char>()
            for (ch in it) {
                when (ch) {
                    '(' -> stack.push(')')
                    '[' -> stack.push(']')
                    '{' -> stack.push('}')
                    '<' -> stack.push('>')
                    stack.pop() -> {}
                    else -> when (ch) {
                        ')' -> return@sumOf 3L
                        ']' -> return@sumOf 57L
                        '}' -> return@sumOf 1197L
                        '>' -> return@sumOf 25137L
                    }
                }
            }
            return@sumOf 0L
        }
    }

    fun part2(input: List<String>): Long {
        val results = input.mapNotNull {
            val stack = Stack<Char>()
            for (ch in it) {
                when (ch) {
                    '(' -> stack.push(')')
                    '[' -> stack.push(']')
                    '{' -> stack.push('}')
                    '<' -> stack.push('>')
                    stack.pop() -> {}
                    else -> return@mapNotNull null
                }
            }
            return@mapNotNull stack.reversed().fold(0L) { result, ch ->
                result * 5 + when (ch) {
                    ')' -> 1L
                    ']' -> 2L
                    '}' -> 3L
                    '>' -> 4L
                    else -> 0L
                }
            }
        }.sorted()
        return results[results.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397L)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
