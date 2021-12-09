import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val coordinates = input.map { line ->
            val split = line.split(" -> ")
            Line(Pair(extractCoordinates(split[0]), extractCoordinates(split[1])))
        }

        val horizontalLines = coordinates.filter { it.lineType == LineType.HORIZONTAL }
        val verticalLines = coordinates.filter { it.lineType == LineType.VERTICAL }

        val expandedHorizontalLines = horizontalLines.flatMap {
            val (x1Position, y1Position) = it.coordinate1
            (x1Position..x1Position + it.delta()).map { yCoordinate ->
                Coordinates(yCoordinate, y1Position)
            }
        }

        val expandedVerticalLines = verticalLines.flatMap {
            val (x1Position, y1Position) = it.coordinate1
            (y1Position..it.delta() + y1Position).map { yCoordinate ->
                Coordinates(x1Position, yCoordinate)
            }
        }

        return listOf(
            *expandedHorizontalLines.toTypedArray(),
            *expandedVerticalLines.toTypedArray(),
        ).groupingBy { it }.eachCount().values.count() { it > 1 }
    }

    fun part2(input: List<String>): Int {
        val coordinates = input.map { line ->
            val split = line.split(" -> ")
            Line(Pair(extractCoordinates(split[0]), extractCoordinates(split[1])))
        }

        val horizontalLines = coordinates.filter { it.lineType == LineType.HORIZONTAL }
        val verticalLines = coordinates.filter { it.lineType == LineType.VERTICAL }
        val diagonalLines = coordinates.filter { it.lineType == LineType.DIAGONAL }

        val expandedHorizontalLines = horizontalLines.flatMap {
            val (x1Position, y1Position) = it.coordinate1
            (x1Position..x1Position + it.delta()).map { yCoordinate ->
                Coordinates(yCoordinate, y1Position)
            }
        }

        val expandedVerticalLines = verticalLines.flatMap {
            val (x1Position, y1Position) = it.coordinate1
            (y1Position..y1Position + it.delta()).map { yCoordinate ->
                Coordinates(x1Position, yCoordinate)
            }
        }

        val expandedDiagonalLines = diagonalLines.flatMap {
            val (x1Position, y1Position) = it.coordinate1
            if (it.coordinate1.y < it.coordinate2.y) {
                (0..it.delta()).map { step ->
                    Coordinates(x1Position + step, y1Position + step)
                }
            } else {
                (0..it.delta()).map { step ->
                    Coordinates(x1Position + step, y1Position - step)
                }
            }
        }

        return listOf(
            *expandedHorizontalLines.toTypedArray(),
            *expandedVerticalLines.toTypedArray(),
            *expandedDiagonalLines.toTypedArray(),
        ).groupingBy { it }.eachCount().values.count() { it > 1 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

private fun extractCoordinates(split: String): Coordinates {
    return split.let {
        val point = it.split(",")
        Coordinates(point[0].toInt(), point[1].toInt())
    }
}

private enum class LineType {
    HORIZONTAL {
        override fun delta(line: Line): Int {
            return abs(line.coordinate1.x - line.coordinate2.x)
        }
    },
    VERTICAL {
        override fun delta(line: Line): Int {
            return abs(line.coordinate1.y - line.coordinate2.y)
        }
    },
    DIAGONAL {
        override fun delta(line: Line): Int {
            return abs(line.coordinate1.y - line.coordinate2.y)
        }
    };

    abstract fun delta(line: Line): Int
}

private data class Coordinates(val x: Int, val y: Int) {}

private class Line(coordinates: Pair<Coordinates, Coordinates>) {

    val lineType: LineType
    lateinit var coordinate1: Coordinates
    lateinit var coordinate2: Coordinates

    init {
        lineType = when {
            coordinates.first.y == coordinates.second.y -> LineType.HORIZONTAL
            coordinates.first.x == coordinates.second.x -> LineType.VERTICAL
            else -> LineType.DIAGONAL
        }

        when (lineType) {
            LineType.HORIZONTAL -> sort(coordinates) { coordinates.first.x < coordinates.second.x }
            LineType.VERTICAL -> sort(coordinates) { coordinates.first.y < coordinates.second.y }
            LineType.DIAGONAL -> sort(coordinates) { coordinates.first.x < coordinates.second.x }
        }
    }

    private fun sort(
        coordinates: Pair<Coordinates, Coordinates>,
        condition: (Pair<Coordinates, Coordinates>) -> Boolean
    ) {
        if (condition(coordinates)) {
            coordinate1 = coordinates.first
            coordinate2 = coordinates.second
        } else {
            coordinate1 = coordinates.second
            coordinate2 = coordinates.first
        }
    }

    fun delta(): Int {
        return lineType.delta(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Line) return false

        if (coordinate1 != other.coordinate1) return false
        if (coordinate2 != other.coordinate2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coordinate1.hashCode()
        result = 31 * result + coordinate2.hashCode()
        return result
    }

}
