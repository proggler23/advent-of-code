import kotlin.math.abs

fun main() {
    fun parse(input: List<String>): List<Scanner> =
        (input.indices.filter { input[it].startsWith("---") } + (input.size + 1)).zipWithNext()
            .map { (start, end) -> input.subList(start + 1, end - 1) }
            .map { beacons ->
                Scanner(
                    beacons = beacons.map {
                        it.split(",").map { coordinate -> coordinate.toInt() }.let { (x, y, z) -> Triple(x, y, z) }
                    }
                )
            }

    fun solve(scanners: List<Scanner>): List<Scanner> {
        val result = mutableListOf(scanners[0])
        val scannersToFit = scanners.drop(1).toMutableList()
        while (scannersToFit.isNotEmpty()) {
            loop@ for (scanner in result) {
                for (scannerToFit in scannersToFit) {
                    for (scannerRotated in scannerToFit.rotations()) {
                        val scannerTranslated = scannerRotated.translated(scanner)
                        if (scannerTranslated != null) {
                            result += scannerTranslated
                            scannersToFit -= scannerToFit
                            break@loop
                        }
                    }
                }
            }
        }
        return result
    }

    fun part1(input: List<String>): Int {
        val scanners = solve(parse(input))
        return scanners.flatMap { it.beacons }.distinct().size
    }

    fun part2(input: List<String>): Int {
        val scanners = solve(parse(input))
        val diffs = scanners.indices.flatMap { i ->
            ((i + 1) until scanners.size).map { j ->
                scanners[i].position.manhatten(scanners[j].position)
            }
        }
        return diffs.maxOrNull()!!
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 79)
    check(part2(testInput) == 3621)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}

fun Triple<Int, Int, Int>.rotations() = listOf(
    Triple(+first, +second, +third),
    Triple(-first, -second, +third),
    Triple(+first, -second, -third),
    Triple(-first, +second, -third),
    Triple(-first, +third, +second),
    Triple(+first, -third, +second),
    Triple(-first, -third, -second),
    Triple(+first, +third, -second),
    Triple(-second, +first, +third),
    Triple(+second, -first, +third),
    Triple(-second, -first, -third),
    Triple(+second, +first, -third),
    Triple(+second, +third, +first),
    Triple(-second, -third, +first),
    Triple(+second, -third, -first),
    Triple(-second, +third, -first),
    Triple(+third, +first, +second),
    Triple(-third, -first, +second),
    Triple(+third, -first, -second),
    Triple(-third, +first, -second),
    Triple(-third, +second, +first),
    Triple(+third, -second, +first),
    Triple(-third, -second, -first),
    Triple(+third, +second, -first)
)

operator fun Triple<Int, Int, Int>.minus(other: Triple<Int, Int, Int>) =
    Triple(first - other.first, second - other.second, third - other.third)

operator fun Triple<Int, Int, Int>.plus(other: Triple<Int, Int, Int>) =
    Triple(first + other.first, second + other.second, third + other.third)

fun Triple<Int, Int, Int>.manhatten(other: Triple<Int, Int, Int>) =
    abs(first - other.first) + abs(second - other.second) + abs(third - other.third)

data class Scanner(
    val beacons: List<Triple<Int, Int, Int>>,
    val position: Triple<Int, Int, Int> = Triple(0, 0, 0)
) {
    fun translated(other: Scanner): Scanner? =
        beacons.flatMap { b -> other.beacons.map { ob -> ob - b } }.groupingBy { it }.eachCount()
            .filter { (_, c) -> c >= 12 }.entries.maxByOrNull { (_, c) -> c }?.key?.let { translate(it) }

    fun rotations(): List<Scanner> {
        val rotated = beacons.map { it.rotations() }
        return rotated[0].indices.map { rotationIndex -> copy(beacons = beacons.indices.map { beaconIndex -> rotated[beaconIndex][rotationIndex] }) }
    }

    private fun translate(translation: Triple<Int, Int, Int>) =
        copy(beacons = beacons.map { it + translation }, position = position + translation)
}
