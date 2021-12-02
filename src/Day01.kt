fun main() {
    fun part1(input: List<String>): Int {
        val measurements = ArrayDeque(input)
        var increaseCount = 0
        var previousMeasurement = measurements.removeFirst()
        while (measurements.isNotEmpty()) {
            if (measurements[0].toInt() > previousMeasurement.toInt()) {
                increaseCount++
            }
            previousMeasurement = measurements.removeFirst()
        }
        return increaseCount
    }

    fun part2(input: List<String>): Int {
        val windowingResult = mutableListOf<String>()
        val windowSize = 3
        var windowSum = 0
        var windowStart = 0

        for ((windowEnd, measurement) in input.withIndex()) {
            windowSum += measurement.toInt()
            if (windowEnd >= windowSize - 1) {
                windowingResult.add(windowSum.toString())
                windowSum -= input[windowStart].toInt()
                windowStart++
            }
        }

        return part1(windowingResult)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
