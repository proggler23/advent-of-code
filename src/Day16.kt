fun main() {
    fun String.hexAsBits() = map { it.toString().toInt(16) }.joinToString("") { it.toString(2).padStart(4, '0') }
    fun List<Packet>.flatten(): List<Packet> = flatMap { it.subPackets.flatten() + it }

    fun part1(input: List<String>): Long {
        val packets = Packet.parse(input[0].hexAsBits())
        return packets.flatten().sumOf { it.version }.toLong()
    }

    fun part2(input: List<String>): Long {
        val packets = Packet.parse(input[0].hexAsBits())
        return packets[0].calculate()
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 16L)
    check(part2(testInput) == 15L)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}

enum class Operation {
    SUM,
    PRODUCT,
    MIN,
    MAX,
    LITERAL,
    GT,
    LT,
    EQ
}

data class Packet(
    val version: Int,
    val operation: Operation,
    val value: Long? = null,
    val subPackets: List<Packet> = emptyList()
) {
    fun calculate(): Long = when (operation) {
        Operation.SUM -> subPackets.sumOf { it.calculate() }
        Operation.PRODUCT -> subPackets.fold(1L) { result, packet -> result * packet.calculate() }
        Operation.MIN -> subPackets.minOf { it.calculate() }
        Operation.MAX -> subPackets.maxOf { it.calculate() }
        Operation.LITERAL -> value ?: error("should never happen")
        Operation.GT -> if (subPackets[0].calculate() > subPackets[1].calculate()) 1L else 0L
        Operation.LT -> if (subPackets[0].calculate() < subPackets[1].calculate()) 1L else 0L
        Operation.EQ -> if (subPackets[0].calculate() == subPackets[1].calculate()) 1L else 0L
    }

    companion object {
        fun parse(input: String, start: Int = 0, end: Int = input.length) = buildList {
            var index = start
            while (index < end) {
                val (packet, newEnd) = parseSingle(input, index) ?: break
                add(packet)
                index = newEnd
            }
        }

        private fun parseSingle(input: String, start: Int): Pair<Packet, Int>? {
            try {
                val version = input.substring(start, start + 3).toInt(2)
                val operation = Operation.values()[input.substring(start + 3, start + 6).toInt(2)]

                if (operation == Operation.LITERAL) {
                    val value = buildString {
                        for (i in start + 6 until input.length step 5) {
                            append(input.substring(i + 1, i + 5))
                            if (input[i] == '0') {
                                break
                            }
                        }
                    }
                    val end = start + 6 + value.length + value.length / 4
                    return Packet(version, operation, value.toLong(2)) to end
                } else {
                    val lengthType = input.substring(start + 6, start + 7).toInt(2)
                    if (lengthType == 0) {
                        val length = input.substring(start + 7, start + 22).toInt(2)
                        val subPackets = parse(input, start + 22, start + 22 + length)
                        return Packet(version, operation, subPackets = subPackets) to start + 22 + length
                    } else if (lengthType == 1) {
                        val count = input.substring(start + 7, start + 18).toInt(2)
                        var index = start + 18
                        val subPackets = buildList {
                            repeat(count) {
                                val (packet, newEnd) = parseSingle(input, index) ?: error("should never happen")
                                add(packet)
                                index = newEnd
                            }
                        }
                        return Packet(version, operation, subPackets = subPackets) to index
                    }
                }
            } catch (_: StringIndexOutOfBoundsException) {
            }
            return null
        }
    }
}
