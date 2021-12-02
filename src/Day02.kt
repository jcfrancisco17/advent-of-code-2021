fun main() {

    fun part1(input: List<String>): Int {
        val course = getVectorAndMagnitude(input)

        var x = 0
        var y = 0

        course.forEach {
            when (it.first) {
                "forward" -> x += it.second
                "up" -> y -= it.second
                "down" -> y += it.second
            }
        }

        return x * y
    }

    fun part2(input: List<String>): Int {
        val course = getVectorAndMagnitude(input)

        var x = 0
        var y = 0
        var aim = 0

        course.forEach {
            when (it.first) {
                "forward" -> {
                    x += it.second
                    y += aim * it.second
                }
                "up" -> aim -= it.second
                "down" -> aim += it.second
            }
        }

        return x * y
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

fun getVectorAndMagnitude(input: List<String>): List<Pair<String, Int>> {
    return input
        .map { it.split(" ") }
        .map { Pair(it[0], it[1].toInt()) }
        .toList()
}
