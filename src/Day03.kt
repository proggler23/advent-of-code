fun main() {
    fun part1(input: List<String>): Int {
        val bitSets = input.map { BitSet(it) }

        val delta = BitSet(bitSets[0].bits.indices.map { i -> bitSets.count { it.bits[i] } >= input.size / 2 })
        val gamma = delta.invert()

        return delta.toInt() * gamma.toInt()
    }

    fun part2(input: List<String>): Int {
        val bitSets = input.map { BitSet(it) }

        val oxygen = bitSets[0].bits.indices.fold(bitSets) { result, bit -> result.takeByBit(bit, true) }[0]
        val co2 = bitSets[0].bits.indices.fold(bitSets) { result, bit -> result.takeByBit(bit, false) }[0]

        return oxygen.toInt() * co2.toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

fun List<BitSet>.takeByBit(bit: Int, most: Boolean) =
    if (size == 1) this else partition { it.bits[bit] }.let { (set, unset) ->
        if (set.size == unset.size) {
            if (set[0].bits[bit] == most) set else unset
        } else {
            if ((set.size > unset.size) == most) set else unset
        }
    }

data class BitSet(val bits: List<Boolean>) {
    constructor(bits: String) : this(bits.toCharArray().map { it == '1' })

    fun invert() = BitSet(bits.map { !it })

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
