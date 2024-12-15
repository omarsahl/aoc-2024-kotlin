package com.omarsahl

import com.omarsahl.base.Puzzle
import com.omarsahl.utils.getPuzzleRealInputFile
import com.omarsahl.utils.times
import java.nio.file.Path
import java.time.LocalDate
import java.time.temporal.ChronoField
import kotlin.reflect.KClass

fun main() {
    PuzzleRunner(LocalDate.now()).run()
}

class PuzzleRunner(private val year: Int, private val day: Int) {

    constructor(date: LocalDate) : this(
        year = date.get(ChronoField.YEAR),
        day = date.get(ChronoField.DAY_OF_MONTH)
    )

    private val headerMessage = "Running puzzle #$day for $year:"

    fun run(parts: IntArray = intArrayOf(1, 2)) {
        printPuzzleHeader()
        val dayStr = day.toString().padStart(2, '0')
        val puzzleClass = Class.forName("${PuzzleRunner::class.java.packageName}.puzzles.y$year.Puzzle$dayStr")
        val puzzle = getPuzzleClassInstance(puzzleClass.kotlin)
        val input = getPuzzleRealInputFile(year, day)

        parts.forEach {
            println("${ANSI_GREEN}Output for part $it:${ANSI_RESET}")
            println(puzzle.solvePart(it, input))
            println()
        }

        printSeparator()
    }

    private fun Puzzle.solvePart(part: Int, input: Path): Any = when (part) {
        1 -> solvePart1(input)
        2 -> solvePart2(input)
        else -> error("Invalid puzzle part number $part")
    }

    private fun getPuzzleClassInstance(puzzleClass: KClass<*>): Puzzle =
        (puzzleClass.objectInstance ?: puzzleClass.constructors.first().call()) as Puzzle

    private fun printPuzzleHeader() {
        println("${ANSI_YELLOW}$headerMessage$ANSI_RESET")
        printSeparator()
    }

    private fun printSeparator() = println("$ANSI_YELLOW${"-" * headerMessage.length}$ANSI_RESET\n")

    companion object {
        private const val ANSI_RESET = "\u001B[0m"
        private const val ANSI_GREEN = "\u001B[92m"
        private const val ANSI_YELLOW = "\u001B[93m"
    }
}
