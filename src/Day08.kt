fun main() {
    val uniqueDigitLengths = setOf(2, 3, 4, 7)

    fun part1(input: List<String>): Int {
        val outputValueDigits = input.flatMap { it.split(" | ").last().split(' ') }
        return outputValueDigits.count { it.length in uniqueDigitLengths }
    }

    fun part2(input: List<String>): Int {
        val outputValueDigits = input.map { it.split(" | ").map { part -> part.split(' ') } }
        return outputValueDigits.sumOf { (code, digits) ->
            val one = code.first { it.length == 2 }
            val four = code.first { it.length == 4 }
            val seven = code.first { it.length == 3 }
            val eight = code.first { it.length == 7 }

            val six = code.first { it.length == 6 && !one.all { oneDigit -> oneDigit in it } }
            val nine = code.first { it.length == 6 && it != six && four.all { fourDigit -> fourDigit in it } }
            val zero = code.first { it.length == 6 && it != six && it != nine }

            val five = code.first { it.length == 5 && it.all { digit -> digit in six } }
            val three = code.first { it.length == 5 && it != five && it.all { digit -> digit in nine } }
            val two = code.first { it.length == 5 && it != five && it != three }

            val digitMappings = mapOf(
                zero to 0,
                one to 1,
                two to 2,
                three to 3,
                four to 4,
                five to 5,
                six to 6,
                seven to 7,
                eight to 8,
                nine to 9
            ).mapKeys { (k) -> k.toCharArray().sorted().joinToString("") }

            digits.mapNotNull { digitMappings[it.toCharArray().sorted().joinToString("")] }.joinToString("").toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
