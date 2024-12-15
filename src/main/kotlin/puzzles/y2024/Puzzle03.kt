package com.omarsahl.puzzles.y2024

import com.omarsahl.base.Puzzle
import java.nio.file.Path
import kotlin.io.path.readLines

object Puzzle03 : Puzzle() {

    override fun solvePart1(input: Path): Long {
        val lines = input.readLines()

        val regex = """mul\((\d{1,3},\d{1,3})\)""".toRegex()
        var result = 0L
        lines.forEach { line ->
            regex.findAll(line).forEach { match ->
                val (x, y) = match.groupValues[1].split(",")
                result += x.toLong() * y.toLong()
            }
        }

        return result
    }

    override fun solvePart2(input: Path): Long {
        val lines = input.readLines()
        val regex = """do\(\)|don't\(\)|mul\((\d{1,3},\d{1,3})\)""".toRegex()
        var result = 0L
        var isOn = true

        lines.forEach { line ->
            regex.findAll(line).forEach { match ->
                when (val value = match.value) {
                    "do()" -> isOn = true
                    "don't()" -> isOn = false
                    else -> {
                        if (isOn) {
                            val (x, y) = value.substring(4, value.lastIndex).split(",")
                            result += (x.toLong() * y.toLong())
                        }
                    }
                }
            }
        }

        return result
    }
}