package com.omarsahl.puzzles

import com.omarsahl.utils.Log
import com.omarsahl.utils.readFileInput
import java.util.*

object Puzzle12 {

    // 1477762
    fun part1() {
        val input = readFileInput("input12.txt")
        Garden(input).calculateTotalFencePrice().also(::println)
    }

    // 923480
    fun part2() {
        val input = readFileInput("input12.txt")
        Garden(input).calculateTotalFenceDiscountedPrice().also(::println)
    }

    private class Garden(input: List<String>) {

        private val grid = Array(input.size) { input[it].toCharArray() }
        private val visited = Array(grid.size) { BooleanArray(grid.size) }
        private val bounds = grid.indices

        fun calculateTotalFencePrice(): Int = allRegions().sumOf { region ->
            region.getArea() * region.getPerimeter()
        }

        fun calculateTotalFenceDiscountedPrice(): Int = allRegions().sumOf { region ->
            region.getArea() * region.getSides()
        }

        private fun allRegions(): Sequence<Region> = sequence {
            forEachPoint { point ->
                if (!isVisited(point)) {
                    val region = traceRegion(point)
                    yield(region)
                }
            }
        }

        private fun traceRegion(start: Point): Region {
            markVisited(start)
            val points = mutableListOf(start)
            val queue = LinkedList<Point>().apply { add(start) }
            while (queue.isNotEmpty()) {
                val current = queue.remove()
                for (direction in Direction.entries) {
                    val next = current.translate(direction)
                    if (current.plot == next.plot && !isVisited(next)) {
                        queue.add(next)
                        points.add(next)
                        markVisited(next)
                    }
                }
            }
            printVisited()
            return Region(points)
        }

        private fun Region.getPerimeter(): Int = points.sumOf { point ->
            Direction.entries.count { direction ->
                val neighbor = point.translate(direction)
                point.plot != neighbor.plot
            }
        }

        private fun Region.getSides(): Int = Direction.entries.sumOf { direction ->
            var sides = 0
            val visited = mutableSetOf<Point>()
            for (point in points) {
                if (point in visited) continue

                val isCorner = point.translate(direction).plot != point.plot
                if (!isCorner) continue

                visited.add(point)
                sides++

                arrayOf(direction.turnLeft(), direction.turnRight()).forEach { newDirection ->
                    var current = point
                    do {
                        current = current.translate(newDirection)
                        visited.add(current)
                    } while (current.plot == point.plot && current.translate(direction).plot != point.plot)
                }
            }
            sides
        }

        private val Point.plot: Char get() = grid.getOrNull(y)?.getOrNull(x) ?: Char.MIN_VALUE

        private fun markVisited(point: Point) {
            visited[point.y][point.x] = true
        }

        private fun isVisited(point: Point): Boolean = visited[point.y][point.x]

        private fun printVisited() {
            Log.d {
                "${
                    visited.joinToString("\n") {
                        it.joinToString("") { bool -> if (bool) "." else "0" }
                    }
                }\n"
            }
        }

        private inline fun forEachPoint(action: (Point) -> Unit) {
            for (y in bounds) {
                for (x in bounds) {
                    val point = Point(x, y)
                    action(point)
                }
            }
        }
    }

    @JvmInline
    private value class Region(val points: List<Point>) {
        fun getArea(): Int = points.size
    }

    private data class Point(val x: Int, val y: Int) {
        fun translate(direction: Direction): Point = copy(
            x = x + direction.dx,
            y = y + direction.dy
        )

        override fun toString(): String = "($x, $y)"
    }

    private enum class Direction(val dx: Int, val dy: Int) {
        Up(0, -1),
        Right(1, 0),
        Down(0, 1),
        Left(-1, 0);

        fun turnLeft() = entries[(ordinal + 3) % entries.size]

        fun turnRight() = entries[(ordinal + 1) % entries.size]
    }
}
