package com.omarsahl.puzzles.y2024

import com.omarsahl.base.Puzzle
import java.nio.file.Path
import kotlin.io.path.readLines

object Puzzle02 : Puzzle() {

    override fun solvePart1(input: Path): Int {
        val lines = input.readLines()
        val result = lines.count { line ->
            val numbers = line.split(" ").map(String::toInt)
            val result = numbers.allIncreasing() || numbers.allDecreasing()
            result
        }

        return result
    }

    override fun solvePart2(input: Path): Int {
        val lines = input.readLines()
        val result = lines.count { line ->
            val numbers = line.split(" ").map(String::toInt)

            if (numbers.allDecreasing() || numbers.allIncreasing()) {
                return@count true
            }

            var reportSafe = false
            for (i in 0..numbers.lastIndex) {
                val numbersSkipped = numbers.toMutableList().apply { removeAt(i) }
                reportSafe = numbersSkipped.allIncreasing() || numbersSkipped.allDecreasing()
                if (reportSafe) break
            }
            reportSafe
        }

        return result
    }

    private fun List<Int>.allIncreasing(): Boolean {
        for (i in lastIndex downTo 1) {
            val diff = this[i] - this[i - 1]
            if (diff !in 1..3) {
                return false
            }
        }
        return true
    }

    private fun List<Int>.allDecreasing(): Boolean {
        for (i in 0..<lastIndex) {
            val diff = this[i] - this[i + 1]
            if (diff !in 1..3) {
                return false
            }
        }
        return true
    }
}