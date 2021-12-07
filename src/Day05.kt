fun main() {

    fun part1(input: List<String>): Int {
        val coordinates = input.map { line ->
            val split = line.split(" -> ")
            Pair(extractCoordinates(split[0]), extractCoordinates(split[1]))
        }

        // get all horizontal lines
        // sort each coordinate pair by ascending X coordinate
        val horizontalLines = coordinates.filter { it.first.y == it.second.y }.map {
            if (it.first.x < it.second.x) {
                Pair(it.first, it.second)
            } else {
                Pair(it.second, it.first)
            }
        }

        // get all horizontal lines
        // sort each coordinate pair by ascending Y coordinate
        val verticalLines = coordinates.filter { it.first.x == it.second.x }.map {
            if (it.first.y < it.second.y) {
                Pair(it.first, it.second)
            } else {
                Pair(it.second, it.first)
            }
        }

        // resolve points in between coordinates
        val expandedVerticalLines = verticalLines.flatMap {
            val expandedCoordinates = mutableListOf<Coordinates>()
            for (newY in it.first.y..it.second.y) {
                expandedCoordinates.add(Coordinates(it.first.x, newY))
            }
            expandedCoordinates
        }

        // resolve points in between coordinates
        val expandedHorizontalLines = horizontalLines.flatMap {
            val expandedCoordinates = mutableListOf<Coordinates>()
            for (xCoordinate in it.first.x..it.second.x) {
                expandedCoordinates.add(Coordinates(xCoordinate, it.first.y))
            }
            expandedCoordinates
        }

        // combine all coordinates in one list
        val combinedLines = listOf(*expandedHorizontalLines.toTypedArray(), *expandedVerticalLines.toTypedArray())

        // put individual coordinates in a map with initial value of 1
        // if coordinate already exists, increment by 1
        val result = mutableMapOf<Coordinates, Int>()
        combinedLines.forEach {
            if (result.computeIfPresent(it) { _, value -> value + 1 } == null) {
                result[it] = 1
            }
        }
        return result.values.count { it > 1 }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 15) // originally 5, added more entries to puzzle input to validate solution
//    check(part2(testInput) == 5)

    val input = readInput("Day05")
    println(part1(input))
//    println(part2(input))
}

private data class Coordinates(val x: Int, val y: Int) {}

private fun extractCoordinates(split: String): Coordinates {
    return split.let {
        val point = it.split(",")
        Coordinates(point[0].toInt(), point[1].toInt())
    }
}