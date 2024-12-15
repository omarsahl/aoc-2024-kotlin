package com.omarsahl.puzzles.y2024

import com.omarsahl.base.Puzzle
import com.omarsahl.utils.isWholeNumber
import java.nio.file.Path
import kotlin.io.path.readLines

object Puzzle13 : Puzzle() {

    // 39290
    override fun solvePart1(input: Path): Long {
        val lines = input.readLines()
        return getMachines(lines).sumOf(ClawMachine::getMinimumTokens)
    }

    // 73458657399094
    override fun solvePart2(input: Path): Long {
        val lines = input.readLines()
        return getMachines(lines, 10000000000000L).sumOf(ClawMachine::getMinimumTokens)
    }

    private fun getMachines(input: List<String>, prizeMultiplier: Long = 0): Sequence<ClawMachine> = sequence {
        for (i in input.indices step 4) {
            val buttonA = parseCoordinates(input[i + 0], "Button A: ")
            val buttonB = parseCoordinates(input[i + 1], "Button B: ")
            val prize = parseCoordinates(input[i + 2], "Prize: ") + prizeMultiplier
            val machine = ClawMachine(buttonA, buttonB, prize)
            yield(machine)
        }
    }

    private fun parseCoordinates(input: String, prefix: String): Coordinates {
        val (x, y) = input.removePrefix(prefix).split(", ").map {
            it.substring(2).toLong()
        }
        return Coordinates(x, y)
    }

    private class ClawMachine(
        private val buttonA: Coordinates,
        private val buttonB: Coordinates,
        private val prize: Coordinates
    ) {

        // https://en.wikipedia.org/wiki/Cramer's_rule
        // tₓ aₓ + tₓ bₓ = zₓ
        // tᵞ aᵞ + tᵞ bᵞ = zᵞ
        // |aₓ bₓ| . |tₓ| = |zₓ|
        // |aᵞ bᵞ|   |tᵞ|   |zᵞ|
        fun getMinimumTokens(): Long {
            val det = SquareMat2(
                buttonA.x, buttonB.x,
                buttonA.y, buttonB.y
            ).determinant()

            val det1 = SquareMat2(
                prize.x, buttonB.x,
                prize.y, buttonB.y
            ).determinant()

            val det2 = SquareMat2(
                buttonA.x, prize.x,
                buttonA.y, prize.y
            ).determinant()

            val tx = det1 / det.toDouble()
            val ty = det2 / det.toDouble()
            if (tx.isWholeNumber() && ty.isWholeNumber()) {
                return tx.toLong() * 3 + ty.toLong()
            }
            return 0
        }
    }

    @JvmInline
    private value class SquareMat2(private val input: LongArray) {

        init {
            require(input.size == SIZE)
        }

        fun determinant(): Long = input[0] * input[3] - input[1] * input[2]

        companion object {
            private const val SIZE = 4

            operator fun invoke(vararg values: Long): SquareMat2 = SquareMat2(values)
        }
    }

    private data class Coordinates(val x: Long, val y: Long) {

        operator fun plus(n: Long): Coordinates = copy(
            x = x + n,
            y = y + n
        )
    }
}
