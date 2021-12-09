fun main() {
    fun part1(input: List<String>): Long {
        val fishCount = mutableMapOf<Int, Long>()
        input[0]
            .split(",")
            .map { it.toInt() }
            .groupingBy { it }
            .eachCount()
            .toMutableMap()
            .forEach { (t, u) ->
                fishCount[t] = u.toLong()
            }

        for (days in 1..256) {
            val zeroCount = fishCount.remove(0) ?: 0
            val toMap = fishCount.map {
                it.key - 1 to it.value
            }.toMap().toMutableMap()

            if (zeroCount > 0) {
                toMap[8] = zeroCount
                toMap[6] = (toMap[6] ?: 0) + zeroCount
            }

            fishCount.clear()
            fishCount.putAll(toMap)
        }

        return fishCount.values.sum()
    }

    val input = readInput("Day06")
    println(part1(input))
}
