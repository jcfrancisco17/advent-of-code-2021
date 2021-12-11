import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val horizontalPositions = input[0].toIntegers()
        val minPosition = horizontalPositions.minOrNull()!!
        val maxPosition = horizontalPositions.maxOrNull()!!

//implementation 1
        val fuelPerPosition = (minPosition..maxPosition)
            .associateWith { position ->
                horizontalPositions.sumOf {
                    abs(it - position)
                }
            }

        return fuelPerPosition.values.minOrNull()!!

//implementation 2
//        val crabPerPosition = horizontalPositions.groupingBy { it }.eachCount()
//        val fuelPerPosition = (minPosition..maxPosition).map { testPosition ->
//            crabPerPosition.map {
//                abs(it.key - testPosition) * it.value
//            }.sum()
//        }
//
//        return fuelPerPosition.minOrNull()!!
    }

    fun part2(input: List<String>): Int {
        val horizontalPositions = input[0].toIntegers()
        val minPosition = horizontalPositions.minOrNull()!!
        val maxPosition = horizontalPositions.maxOrNull()!!

// implementation 1
        val fuelPerPosition = (minPosition..maxPosition).associateWith { position ->
            horizontalPositions.sumOf {
                val steps = abs(it - position)
                (0 until steps).sumOf { step -> step + 1 }
            }
        }
        return fuelPerPosition.values.minOrNull()!!

//implementation 2
//        val crabPerPosition = numbers.groupingBy { it }.eachCount()
//        val fuelPerPosition = (minPosition..maxPosition).map { testPosition ->
//            crabPerPosition.map {
//                val steps = abs(it.key - testPosition)
//                ((0 until steps).sumOf { step -> step + 1 }) * it.value
//            }.sum()
//        }

//        return fuelPerPosition.minOrNull()!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private fun String.toIntegers(): List<Int> {
    return split(",").map { it.toInt() }
}