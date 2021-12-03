fun main() {
    fun part1(input: List<String>): Int {
        val bitCounts = input[0].toCharArray().map { 0 }.toMutableList()
        input.map { BitSet(it) }.forEach {
            it.bits.forEachIndexed { i, bit ->
                if (bit) {
                    bitCounts[i] = bitCounts.getOrElse(i) { 0 } + 1
                }
            }
        }

        val deltaBits = bitCounts.map { count -> count > input.size / 2 }
        val gammaBits = bitCounts.map { count -> count < input.size / 2 }

        return BitSet(deltaBits).toInt() * BitSet(gammaBits).toInt()
    }

    fun part2(input: List<String>): Int {
        var bitSets = input.map { BitSet(it) }
        var i = 0
        while (bitSets.size > 1) {
            val (set, unset) = bitSets.partition { it.bits[i] }
            bitSets = if (set.size == unset.size) {
                if (set[0].bits[i]) set else unset
            } else {
                if (set.size > unset.size) set else unset
            }
            i++
        }
        val oxygen = bitSets[0].toInt()

        bitSets = input.map { BitSet(it) }
        i = 0
        while (bitSets.size > 1) {
            val (set, unset) = bitSets.partition { it.bits[i] }
            bitSets = if (set.size == unset.size) {
                if (set[0].bits[i]) unset else set
            } else {
                if (set.size < unset.size) set else unset
            }
            i++
        }
        val c02 = bitSets[0].toInt()
        return oxygen * c02
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

data class BitSet(val bits: List<Boolean>) {
    constructor(bits: String) : this(bits.toCharArray().map { it == '1' })

    fun toInt(): Int {
        return bits.mapIndexed { i, bit -> if (bit) 2.pow(bits.size - i - 1) else 0 }.sum()
    }

    private fun Int.pow(power: Int): Int {
        var result = 1
        for (i in (1..power)) {
            result *= this
        }
        return result
    }
}
