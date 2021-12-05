private val rowsPerCard = 5

fun main() {

    fun part1(input: List<String>): Int {
        val puzzleInput = preparePuzzleInput(input)
        val drawnNumbers = extractDrawnNumbers(puzzleInput)
        val bingoCards = createBingoCards(puzzleInput)

        var winningNumber = 0

        val winningCards = mutableListOf<BingoCard>()
        for (number in drawnNumbers) {
            findMatchInBingoCards(bingoCards, number)
            bingoCards.forEach { bingoCard ->
                if (foundWinningRowOrColumn(bingoCard)) {
                    winningCards.add(bingoCard)
                    winningNumber = number
                }
            }
            if (winningCards.isNotEmpty()) {
                break
            }
        }

        return winningNumber * winningCards.first().sumOfUnmarkedNumbers()
    }

    fun part2(input: List<String>): Int {
        val puzzleInput = preparePuzzleInput(input)
        val drawnNumbers = extractDrawnNumbers(puzzleInput)
        val bingoCards = createBingoCards(puzzleInput)

        var winningNumber = 0
        val numberOfBingoCards = bingoCards.size
        val winningCards = mutableListOf<BingoCard>()
        for (drawnNumber in drawnNumbers) {
            if (bingoCards.isNotEmpty()) {
                findMatchInBingoCards(bingoCards, drawnNumber)
                for (bingoCard in bingoCards) {
                    if (foundWinningRowOrColumn(bingoCard)) {
                        winningCards.add(bingoCard)
                    }
                }
                bingoCards.removeAll(winningCards)
            }
            if (numberOfBingoCards == winningCards.size) {
                winningNumber = drawnNumber
                break
            }
        }
        return winningNumber * winningCards.last().sumOfUnmarkedNumbers()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))


}

class BingoCard(val id: Int, lines: List<String>) {

    private var matrix = mutableListOf<Cell>()
    private val rows = mutableListOf<List<Cell>>()
    private val columns = mutableListOf<List<Cell>>()

    companion object {
        const val DOUBLE_SPACE = "  "
        const val SINGLE_SPACE = " "
    }

    init {
        lines.withIndex().forEach { (y, numbers) ->
            matrix.addAll(
                numbers
                    .trim()
                    .replace(DOUBLE_SPACE, SINGLE_SPACE)
                    .split(SINGLE_SPACE)
                    .withIndex()
                    .map { (x, value) -> Cell(Position(x, y), value.toInt()) }.toList()
            )
        }
        rows.addAll(matrix.groupBy { cell -> cell.position.y }.values)
        columns.addAll(matrix.groupBy { cell -> cell.position.x }.values)
    }

    fun markIfMatch(drawnNumber: Int) {
        matrix.find { number -> number.value == drawnNumber }?.mark()
    }

    fun checkRows(): Boolean {
        return checkForMatch(this.rows)
    }

    fun checkColumns(): Boolean {
        return checkForMatch(this.columns)
    }

    private fun checkForMatch(numbers: MutableList<List<Cell>>): Boolean {
        var foundWinner = false
        for (number in numbers) {
            if (number.count { number -> number.marked } == rowsPerCard) {
                foundWinner = true
                break
            }
        }
        return foundWinner
    }

    fun sumOfUnmarkedNumbers(): Int {
        return this.matrix.filter { cell -> !cell.marked }.sumOf { cell -> cell.value }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BingoCard) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}

class Cell(val position: Position, val value: Int, var marked: Boolean = false) {

    fun mark() {
        this.marked = true
    }
}

class Position(val x: Int, val y: Int) {}

fun createBingoCards(inputQueue: ArrayDeque<String>): MutableList<BingoCard> {
    val bingoCards = mutableListOf<BingoCard>()
    var cardId = 0
    while (inputQueue.isNotEmpty()) {
        val cardNumbers = inputQueue.take(rowsPerCard)
        bingoCards.add(BingoCard(cardId, cardNumbers))
        inputQueue.removeAll(cardNumbers)
        cardId++
    }
    return bingoCards
}

fun foundWinningRowOrColumn(bingoCard: BingoCard) = bingoCard.checkRows() || bingoCard.checkColumns()

fun findMatchInBingoCards(bingoCards: MutableList<BingoCard>, number: Int) {
    bingoCards.forEach { bingoCard ->
        bingoCard.markIfMatch(number)
    }
}

fun extractDrawnNumbers(inputQueue: ArrayDeque<String>) =
    inputQueue.removeFirst().split(",").map { it.toInt() }.toList()

fun preparePuzzleInput(input: List<String>): ArrayDeque<String> {
    val puzzleInput = ArrayDeque(input)
    puzzleInput.removeAll { line -> line == "" } //remove empty strings
    return puzzleInput
}