package com.omarsahl.puzzles

import com.omarsahl.readFileInput

object Puzzle4 {

    class Part1(private val grid: Array<CharArray>) {
        private val query = "XMAS"
        private val boundaries = grid.indices
        private val directions = listOf(
            Pair(+1, +0),
            Pair(-1, +0),
            Pair(+0, +1),
            Pair(+0, -1),
            Pair(+1, +1),
            Pair(-1, -1),
            Pair(-1, +1),
            Pair(+1, -1),
        )

        fun run() {
            var result = 0
            for (y in boundaries) {
                for (x in boundaries) {
                    for ((dx, dy) in directions) {
                        if (expandInDirection(grid, x, y, dx, dy, boundaries)) {
                            result++
                        }
                    }
                }
            }
            println(result)
        }

        private fun expandInDirection(
            grid: Array<CharArray>,
            x: Int,
            y: Int,
            dx: Int,
            dy: Int,
            boundaries: IntRange
        ): Boolean {
            for (i in query.indices) {
                val cx = x + i * dx
                val cy = y + i * dy

                if (cx !in boundaries || cy !in boundaries || grid[cy][cx] != query[i]) {
                    return false
                }
            }

            return true
        }
    }

    fun part1() {
        val input = readFileInput("input4.txt")
        val grid = Array(input.size) { input[it].toCharArray() }
        Part1(grid).run()
    }

    class Part2(private val grid: Array<CharArray>) {
        private val boundaries = grid.indices
        private val leftDiagonal = arrayOf(
            Pair(-1, -1),
            Pair(+1, +1),
        )
        private val rightDiagonal = listOf(
            Pair(+1, -1),
            Pair(-1, +1),
        )
        private val diagonalsRange = leftDiagonal.indices
        private val diagonalSteps = arrayOf(+1, 0, -1)
        private val mas = charArrayOf('M', 'A', 'S')
        private val sam = charArrayOf('S', 'A', 'M')

        fun run() {
            var result = 0
            for (y in boundaries) {
                for (x in boundaries) {
                    if (grid[y][x] == 'A' && expandFromCenter(x, y)) {
                        result++
                    }
                }
            }
            println(result)
        }

        private fun expandFromCenter(centerX: Int, centerY: Int): Boolean {
            for (i in diagonalsRange) {
                val (dx1, dy1) = leftDiagonal[i]
                val isMasLeft = matches(centerX, centerY, dx1, dy1, mas) || matches(centerX, centerY, dx1, dy1, sam)
                if (!isMasLeft) return false

                val (dx2, dy2) = rightDiagonal[i]
                val isMasRight = matches(centerX, centerY, dx2, dy2, mas) || matches(centerX, centerY, dx2, dy2, sam)
                return isMasRight
            }
            return false
        }

        private fun matches(centerX: Int, centerY: Int, dx: Int, dy: Int, q: CharArray): Boolean {
            for (i in diagonalSteps.indices) {
                val s = diagonalSteps[i]
                val x = centerX + dx * s
                val y = centerY + dy * s

                if (x !in boundaries || y !in boundaries || grid[y][x] != q[i]) {
                    return false
                }
            }
            return true
        }
    }

    fun part2() {
        val input = readFileInput("input4.txt")
        val grid = Array(input.size) { input[it].toCharArray() }
        Part2(grid).run()
    }
}