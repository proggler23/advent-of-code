import kotlin.math.abs
import kotlin.math.min

fun main() {
    fun solve(input: List<String>): Int {
        val states = mutableListOf(State.parse(input))
        var minCost = Int.MAX_VALUE
        while (states.isNotEmpty()) {
            val state = states.removeLast()
            if (state.isFinished()) {
                minCost = min(minCost, state.stepCosts)
                println(minCost)
            } else {
                states += state.nextSteps().filter { it.stepCosts < minCost }
            }
        }
        return minCost
    }

    fun part1(input: List<String>) = solve(input)

    fun part2(input: List<String>) = solve(
        input.take(3) + listOf("  #D#C#B#A#", "  #D#B#A#C#") + input.takeLast(2)
    )


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 12521)
    check(part2(testInput) == 44169)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}

val roomToPodType = mapOf(
    LocationType.ROOM1 to AmphipodType.A,
    LocationType.ROOM2 to AmphipodType.B,
    LocationType.ROOM3 to AmphipodType.C,
    LocationType.ROOM4 to AmphipodType.D
)

val podTypeToRoom = roomToPodType.entries.associate { (k, v) -> v to k }

enum class LocationType(val hallwayIndex: Int?) {
    ROOM1(2), ROOM2(4), ROOM3(6), ROOM4(8), HALLWAY(null);
}

data class Location(val type: LocationType, val place: Int)

enum class AmphipodType(val stepCost: Int) {
    A(1), B(10), C(100), D(1000), NONE(-1);
}

data class Amphipod(val type: AmphipodType, val position: Location, val steps: Int = 0) {
    val stepCosts = steps * type.stepCost

    fun nextSteps(state: State): List<Amphipod> {
        val hallwayIndex = position.type.hallwayIndex ?: position.place
        val lockedHallwayLeft: Int by lazy { state.lockedHallwayIndices.lastOrNull { it < hallwayIndex } ?: -1 }
        val lockedHallwayRight: Int by lazy { state.lockedHallwayIndices.firstOrNull { it > hallwayIndex } ?: 11 }
        val hallwayPositions: List<Int> by lazy {
            ((lockedHallwayLeft + 1 until hallwayIndex) + (hallwayIndex + 1 until lockedHallwayRight)).filter {
                it !in LocationType.values().mapNotNull { l -> l.hallwayIndex }
            }
        }
        return if (isFinished(state)) emptyList()
        else if (position.type == LocationType.HALLWAY) {
            val neededRoom = podTypeToRoom[type]!!
            val podsInRoom = state.podsByRoom[neededRoom].orEmpty()
            if (podsInRoom.all { it.type == type } && (if (neededRoom.hallwayIndex!! > position.place) lockedHallwayRight > neededRoom.hallwayIndex else lockedHallwayLeft < neededRoom.hallwayIndex)) {
                listOf(
                    copy(
                        position = Location(neededRoom, state.amphipods.size / 4 - podsInRoom.size),
                        steps = steps + abs(hallwayIndex - neededRoom.hallwayIndex) + (state.amphipods.size / 4 - podsInRoom.size)
                    )
                )
            } else emptyList()
        } else if (state.podsByRoom[position.type].orEmpty().any { it.position.place < position.place }) {
            emptyList()
        } else {
            hallwayPositions.map {
                copy(
                    position = Location(LocationType.HALLWAY, it), steps = position.place + abs(it - hallwayIndex)
                )
            }
        }
    }

    fun isFinished(state: State) = roomToPodType[position.type] == type &&
            state.podsByRoom[position.type]!!.filter { it.position.place > position.place }.all { it.type == type }
}

data class State(val amphipods: List<Amphipod>) {
    val podsByRoom = amphipods.groupBy { it.position.type }

    val lockedHallwayIndices: List<Int> by lazy {
        podsByRoom[LocationType.HALLWAY].orEmpty().map { it.position.place }.sorted()
    }

    val stepCosts = amphipods.sumOf { it.stepCosts }

    fun nextSteps() = amphipods.flatMapIndexed { i, pod ->
        pod.nextSteps(this).map { newPod ->
            State(amphipods.toMutableList().also { it[i] = newPod })
        }
    }

    fun isFinished() = amphipods.all { it.isFinished(this) }

    companion object {
        fun parse(input: List<String>) =
            State(amphipods = input.drop(2).dropLast(1).flatMapIndexed { i: Int, line: String ->
                (3..9 step 2).mapIndexed { j, index ->
                    Amphipod(
                        type = AmphipodType.valueOf(line[index].toString()),
                        position = Location(LocationType.values()[j], i + 1)
                    )
                }
            })
    }
}
