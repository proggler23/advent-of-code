import Cuboid.Companion.merge
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>) = input.map { Cuboid.parse(it) }.fold(emptySet<Cube>()) { cubes, (cuboid, on) ->
        if (on) cubes + cuboid.cubes
        else cubes - cuboid.cubes
    }.size

    fun part2(input: List<String>) = input.map { Cuboid.parse(it) }.fold(emptyList<Cuboid>()) { cuboids, (cuboid, on) ->
        if (on) cuboids.fold(listOf(cuboid)) { result, c -> result.flatMap { r -> r - c } }.merge() + cuboids
        else cuboids.flatMap { it - cuboid }
    }.sumOf { it.size }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 474140)
    check(part2(testInput) == 2758514936282235L)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}

typealias Cube = Triple<Int, Int, Int>

data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange) {
    val cubes = buildSet {
        for (x in max(-50, x.first)..min(50, x.last)) {
            for (y in max(-50, y.first)..min(50, y.last)) {
                for (z in max(-50, z.first)..min(50, z.last)) {
                    add(Cube(x, y, z))
                }
            }
        }
    }

    val size: Long get() = x.count().toLong() * y.count().toLong() * z.count().toLong()

    operator fun plus(other: Cuboid) =
        if (intersects(other)) allParts(other).filter { it.intersects(this) || it.intersects(other) }.merge()
        else listOf(this)

    operator fun minus(other: Cuboid) =
        if (intersects(other)) allParts(other).filter { it.intersects(this) && !it.intersects(other) }.merge()
        else listOf(this)

    private fun intersects(other: Cuboid) = listOf(x, y, z).zip(listOf(other.x, other.y, other.z))
        .all { (range, otherRange) -> range.intersects(otherRange) }

    private fun canMerge(other: Cuboid): Boolean {
        val r = listOf(x, y, z).zip(listOf(other.x, other.y, other.z))
        return pos.any { (a, b, c) -> r[a].first == r[a].second && r[b].first == r[b].second && r[c].first.adjacentTo(r[c].second) }
    }

    private fun merge(other: Cuboid) = when {
        x.adjacentTo(other.x) -> copy(x = min(x.first, other.x.first)..max(x.last, other.x.last))
        y.adjacentTo(other.y) -> copy(y = min(y.first, other.y.first)..max(y.last, other.y.last))
        z.adjacentTo(other.z) -> copy(z = min(z.first, other.z.first)..max(z.last, other.z.last))
        else -> error("should never happen")
    }

    private fun allParts(other: Cuboid) = x.allParts(other.x).flatMap { xRange ->
        y.allParts(other.y).flatMap { yRange ->
            z.allParts(other.z).map { zRange ->
                Cuboid(xRange, yRange, zRange)
            }
        }
    }

    private fun IntRange.adjacentTo(other: IntRange) = min(last, other.last) + 1 == max(first, other.first)

    private fun IntRange.intersects(other: IntRange) = min(last, other.last) >= max(first, other.first)

    private fun IntRange.allParts(other: IntRange) = listOf(
        min(first, other.first) until max(first, other.first),
        max(first, other.first)..min(last, other.last),
        min(last, other.last) + 1..max(last, other.last)
    ).filterNot { it.isEmpty() }

    companion object {
        val pos = listOf(
            Triple(0, 1, 2), Triple(0, 2, 1), Triple(1, 0, 2), Triple(1, 2, 1), Triple(2, 0, 1), Triple(2, 1, 0)
        )

        fun parse(line: String): Pair<Cuboid, Boolean> {
            val on = line.startsWith("on")
            val prefixLength = if (on) "on ".length else "off ".length
            val (rangeX, rangeY, rangeZ) = line.substring(prefixLength).split(",")
                .map { part -> part.substring("x=".length).split("..").map { range -> range.toInt() } }
            return Cuboid(
                x = rangeX.minOrNull()!!..rangeX.maxOrNull()!!,
                y = rangeY.minOrNull()!!..rangeY.maxOrNull()!!,
                z = rangeZ.minOrNull()!!..rangeZ.maxOrNull()!!,
            ) to on
        }

        fun List<Cuboid>.merge(): List<Cuboid> {
            val cuboids = this.toMutableList()
            generateSequence {
                cuboids.indices.firstNotNullOfOrNull { i ->
                    (i + 1 until cuboids.size).firstNotNullOfOrNull { j ->
                        if (cuboids[i].canMerge(cuboids[j])) cuboids[i] to cuboids[j] else null
                    }
                }
            }.forEach { (c1, c2) ->
                cuboids -= setOf(c1, c2)
                cuboids += c1.merge(c2)
            }
            return cuboids
        }
    }
}
