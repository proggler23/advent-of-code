fun main() {
    val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)

    fun getShortestPath(risks: List<List<Int>>): Int {
        val unsettled = mutableSetOf(0 to 0)
        val settled = mutableSetOf<Pair<Int, Int>>()
        val nodes = buildSet {
            for (y in risks.indices) {
                for (x in risks[y].indices) {
                    add(x to y)
                }
            }
        }
        val edges = nodes.associateWith { (x, y) ->
            directions.mapNotNull { (dx, dy) ->
                val nextX = x + dx
                val nextY = y + dy
                val risk = risks.getOrNull(nextY)?.getOrNull(nextX)
                risk?.let { nextX to nextY }
            }.toSet()
        }
        val weights = nodes.associateWith { Int.MAX_VALUE }.toMutableMap()

        weights[0 to 0] = 0

        while (unsettled.isNotEmpty()) {
            val evalNode = unsettled.minWithOrNull(Comparator.comparing { node -> weights[node]!! })!!.also {
                unsettled.remove(it)
                settled.add(it)
            }
            val risk = weights[evalNode]!!
            for (dest in edges[evalNode]!!) {
                if (dest !in settled && dest !in unsettled) {
                    weights[dest] = risk + risks[dest.second][dest.first]
                    unsettled.add(dest)
                }
            }
        }

        return weights[risks.size - 1 to risks[0].size - 1]!!
    }

    fun part1(input: List<String>): Int {
        val risks = input.map { line -> line.toCharArray().map { it.digitToInt() } }
        return getShortestPath(risks)
    }

    fun part2(input: List<String>): Int {
        val risks = input.map { line -> line.toCharArray().map { it.digitToInt() } }

        val fullRisks = (0 until 5).flatMap { repY ->
            risks.map { row ->
                (0 until 5).flatMap { repX ->
                    row.map { r -> ((r + repX + repY - 1) % 9) + 1 }
                }
            }
        }

        return getShortestPath(fullRisks)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
