package com.omarsahl.puzzles

import com.omarsahl.utils.readFileInput

object Puzzle3 {

    fun part1() {
        val lines = readFileInput("input3.txt")

        val regex = """mul\((\d{1,3},\d{1,3})\)""".toRegex()
        var result = 0L
        lines.forEach { line ->
            regex.findAll(line).forEach { match ->
                val (x, y) = match.groupValues[1].split(",")
                result += x.toLong() * y.toLong()
            }
        }

        println(result)
    }

    fun part2() {
        val input = readFileInput("input3.txt")
        val regex = """do\(\)|don't\(\)|mul\((\d{1,3},\d{1,3})\)""".toRegex()
        var result = 0L
        var isOn = true

        input.forEach { line ->
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

        println(result)
    }
}