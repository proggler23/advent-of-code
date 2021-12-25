fun main() {
    fun List<String>.lastOf(command: String) = last { it.startsWith(command) }.split(" ").last().toInt()
    fun solve(input: List<String>, part2: Boolean = false): Long {
        val result = MutableList(14) { -1 }
        val buffer = mutableListOf<Pair<Int, Int>>()
        input.chunked(18).forEachIndexed { index, instructions ->
            if ("div z 26" in instructions) {
                val offset = instructions.lastOf("add x")
                val (lastIndex, lastOffset) = buffer.removeFirst()
                val difference = offset + lastOffset
                if (difference >= 0) {
                    result[lastIndex] = if (part2) 1 else 9 - difference
                    result[index] = if (part2) 1 + difference else 9
                } else {
                    result[lastIndex] = if (part2) 1 - difference else 9
                    result[index] = if (part2) 1 else 9 + difference
                }
            } else buffer.add(0, index to instructions.lastOf("add y"))
        }

        return result.joinToString("").toLong()
    }

    fun part1(input: List<String>) = solve(input)
    fun part2(input: List<String>) = solve(input, true)

// test if implementation meets criteria from the description, like:

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
