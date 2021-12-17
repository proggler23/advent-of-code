import kotlin.math.abs
import kotlin.math.max

fun main() {
    fun parseArea(input: String) = input.substring("target area: ".length).split(", ")
        .map { it.substring("x=".length).split("..").map { v -> v.toInt() } }.let { (x, y) ->
            Area(Point(x.minOrNull()!!, y.minOrNull()!!), Point(x.maxOrNull()!!, y.maxOrNull()!!))
        }

    fun findSolutions(area: Area) = buildSet {
        for (vx in area.max.x downTo 1) {
            for (vy in area.min.y..abs(area.min.y) * 2) {
                val probe = Probe.stepUntilInAreaOrWide(Probe(vel = Point(vx, vy)), area)
                if (probe.pos.isInside(area)) add(probe)
                if (probe.hasOvershot(area)) break
            }
        }
    }

    fun part1(input: List<String>) = findSolutions(parseArea(input[0])).maxOf { it.maxY }
    fun part2(input: List<String>) = findSolutions(parseArea(input[0])).map { it.initialVel }.toSet().size

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}

data class Point(val x: Int, val y: Int) {
    fun isInside(area: Area) = x in area.min.x..area.max.x && y in area.min.y..area.max.y
    fun isWide(area: Area) = x > area.max.x || y < area.min.y

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
}

data class Area(val min: Point, val max: Point)

data class Probe(val pos: Point = Point(0, 0), val vel: Point, val maxY: Int = 0, val initialVel: Point = vel) {
    companion object {
        tailrec fun stepUntilInAreaOrWide(probe: Probe, area: Area): Probe {
            if (probe.pos.isInside(area) || probe.pos.isWide(area) || probe.hasOvershot(area)) {
                return probe
            }
            return stepUntilInAreaOrWide(probe.step(), area)
        }
    }

    fun hasOvershot(area: Area) = abs(vel.y) / 4 > area.max.y - area.min.y || (vel.x == 0 && pos.x < area.min.x)

    private fun step() = copy(
        pos = pos + vel,
        vel = Point(if (vel.x == 0) 0 else if (vel.x < 0) vel.x + 1 else vel.x - 1, vel.y - 1),
        maxY = max(maxY, (pos + vel).y)
    )
}
