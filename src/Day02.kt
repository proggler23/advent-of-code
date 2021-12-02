fun main() {
    fun part1(input: List<String>): Int {
        val position = input.map(Movement.Companion::parse).fold(Position()) { result, movement ->
            movement.move(result)
        }
        return position.forward * position.down
    }

    fun part2(input: List<String>): Int {
        val position = input.map(Movement.Companion::parse).fold(Position()) { result, movement ->
            movement.move2(result)
        }
        return position.forward * position.down
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 144)
    check(part2(testInput) == 1296)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

data class Position(val forward: Int = 0, val down: Int = 0, val aim: Int = 0)

enum class Direction {
    FORWARD,
    DOWN,
    UP
}

data class Movement(val direction: Direction, val distance: Int) {
    fun move(position: Position): Position {
        return when (direction) {
            Direction.FORWARD -> position.copy(forward = position.forward + distance)
            Direction.DOWN -> position.copy(down = position.down + distance)
            Direction.UP -> position.copy(down = position.down - distance)
        }
    }

    fun move2(position: Position): Position {
        return when (direction) {
            Direction.FORWARD -> position.copy(
                forward = position.forward + distance,
                down = position.down + distance * position.aim
            )
            Direction.DOWN -> position.copy(aim = position.aim + distance)
            Direction.UP -> position.copy(aim = position.aim - distance)
        }
    }

    companion object {
        fun parse(line: String): Movement {
            return line.split(" ").let { (direction, distance) ->
                Movement(
                    direction = Direction.valueOf(direction.uppercase()),
                    distance = distance.toInt()
                )
            }
        }
    }
}
