import kotlin.text.StringBuilder

fun main() {

    fun part1(input: List<String>): Int {
        val groupedByIndex = mutableMapOf<Int, MutableList<Int>>()

        input.forEach {
            for ((index, bit) in it.toCharArray().withIndex()) {
                if (groupedByIndex[index] == null) {
                    groupedByIndex[index] = mutableListOf(bit.toString().toInt())
                } else {
                    groupedByIndex[index]!!.add(bit.toString().toInt())
                }
            }
        }

        val gamma = StringBuilder()
        val epsilon = StringBuilder()
        groupedByIndex.forEach {
            val oneCount = it.value.count { bit -> bit == 1 }
            val zeroCount = it.value.count { bit -> bit == 0 }
            if (oneCount > zeroCount) {
                gamma.append(1)
                epsilon.append(0)
            } else {
                gamma.append(0)
                epsilon.append(1)
            }
        }

        return gamma.toString().toInt(2) * epsilon.toString().toInt(2)
    }

    fun part2(input: List<String>): Int {
        val oxygenCount = rating(input) { ones, zeroes -> ones >= zeroes }
        val co2Count = rating(input) { ones, zeroes -> ones < zeroes }
        return co2Count * oxygenCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

fun rating(input: List<String>, test: (Int, Int) -> Boolean): Int {
    val data = mutableListOf(*input.toTypedArray())
    val dataLength = input[0].length
    for (index in 0..dataLength) {
        if (data.size == 1) {
            return data[0].toInt(2)
        }
        val ones = data.count { it.toCharArray()[index] == '1' }
        val zeroes = data.count { it.toCharArray()[index] == '0' }
        if (test(ones, zeroes)) {
            data.retainAll { it.toCharArray()[index] == '1' }
        } else {
            data.retainAll { it.toCharArray()[index] == '0' }
        }
    }
    return (data[0].toInt(2))
}
