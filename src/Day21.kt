import kotlin.math.min

fun main() {
    fun playerPosition(input: String) = input.substring("Player x starting position: ".length).toInt()
    fun parse(input: List<String>) = GameState(playerPosition(input[0]), playerPosition(input[1]), 0, 0, true)
    val diracRolls = (1..3).flatMap { r1 -> (1..3).flatMap { r2 -> (1..3).map { r3 -> r1 + r2 + r3 } } }
        .groupingBy { it }.eachCount()

    fun deterministicRolls(rollCount: Int) = mapOf((rollCount + 1..rollCount + 3).sum() to 1)

    fun part1(input: List<String>) =
        generateSequence(parse(input)) { it.roll3Times(deterministicRolls(it.rollCount))[0] }
            .first { it.isFinished(1000) }
            .let { min(it.player1Score, it.player2Score) * it.rollCount }

    fun part2(input: List<String>): Long {
        val gameStates = mutableListOf(parse(input))
        var (wins, looses) = 0L to 0L
        while (gameStates.isNotEmpty()) {
            val gameState = gameStates.removeLast()
            if (gameState.isFinished(21)) {
                if (!gameState.p1Turn) {
                    wins += gameState.same
                } else {
                    looses += gameState.same
                }
            } else {
                gameStates += gameState.roll3Times(diracRolls)
            }
        }
        return wins
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 739785)
    check(part2(testInput) == 444356092776315L)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}

data class GameState(
    val player1: Int,
    val player2: Int,
    val player1Score: Int,
    val player2Score: Int,
    val p1Turn: Boolean,
    val same: Long = 1L,
    val rollCount: Int = 0
) {
    fun isFinished(winScore: Int) = player1Score >= winScore || player2Score >= winScore

    fun roll3Times(rolls: Map<Int, Int>) = rolls.map { (dice, count) ->
        var (p1, p2, p1Score, p2Score) = this
        if (p1Turn) {
            p1 = 1 + (p1 - 1 + dice) % 10
            p1Score += p1
        } else {
            p2 = 1 + (p2 - 1 + dice) % 10
            p2Score += p2
        }
        GameState(p1, p2, p1Score, p2Score, !p1Turn, same * count, rollCount + 3)
    }
}
