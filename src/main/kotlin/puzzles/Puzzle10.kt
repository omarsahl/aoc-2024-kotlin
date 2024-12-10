package com.omarsahl.puzzles

import com.omarsahl.utils.isBitSet
import com.omarsahl.utils.readFileInput
import com.omarsahl.utils.setBit
import java.util.*

object Puzzle10 {

    // 737
    fun part1() {
        val input = readFileInput("input10.txt")
        val totalScore = TopographicMap(input).getTotalTrailheadsScore()
        println(totalScore)
    }

    // 1619
    fun part2() {
        val input = readFileInput("input10.txt")
        val totalRating = TopographicMap(input).getTotalTrailheadsRating()
        println(totalRating)
    }

    private class TopographicMap(input: List<String>) {

        private val bounds: IntRange
        private val grid: Array<IntArray>
        private val trailheadCandidates: List<Position>
        private val visitedCache: LongArray

        init {
            val trailheadCandidates = mutableListOf<Position>()
            this.grid = Array(input.size) { y ->
                IntArray(input.size) { x ->
                    input[y][x].digitToInt().also {
                        if (it == TRAIL_START) trailheadCandidates.add(Position(x, y))
                    }
                }
            }
            this.bounds = grid.indices
            this.trailheadCandidates = trailheadCandidates
            this.visitedCache = LongArray(grid.size)
        }

        fun getTotalTrailheadsScore(): Int = trailheadCandidates.sumOf(::getTrailheadScore)

        fun getTotalTrailheadsRating(): Int = trailheadCandidates.sumOf(::getTrailheadRating)

        private fun getTrailheadScore(start: Position): Int {
            var score = 0
            resetVisitedCache()
            markVisited(start)
            exploreFrom(
                start = start,
                trailEnd = { score++ },
                onVisit = { markVisited(it) },
                shouldVisit = { current, next -> getHeight(next) == getHeight(current) + 1 && !isVisited(next) }
            )
            return score
        }

        private fun getTrailheadRating(start: Position): Int {
            var rating = 0
            exploreFrom(
                start = start,
                trailEnd = { rating++ },
                shouldVisit = { current, next -> getHeight(next) == getHeight(current) + 1 }
            )
            return rating
        }

        private inline fun exploreFrom(
            start: Position,
            trailEnd: () -> Unit,
            shouldVisit: (current: Position, next: Position) -> Boolean,
            onVisit: (Position) -> Unit = {},
        ) {
            val stack = Stack<Position>().apply {
                add(start)
            }

            while (stack.isNotEmpty()) {
                val current = stack.pop()
                if (getHeight(current) == TRAIL_END) {
                    trailEnd()
                    continue
                }

                for (direction in Direction.entries) {
                    val next = current.translate(direction)
                    if (next.inBounds() && shouldVisit(current, next)) {
                        stack.add(next)
                        onVisit(next)
                    }
                }
            }
        }

        private fun getHeight(position: Position): Int = grid[position.y][position.x]

        private fun markVisited(position: Position) {
            visitedCache[position.y] = visitedCache[position.y].setBit(position.x)
        }

        private fun isVisited(position: Position): Boolean = visitedCache[position.y].isBitSet(position.x)

        private fun resetVisitedCache() {
            for (i in visitedCache.indices) {
                visitedCache[i] = 0
            }
        }

        private fun Position.inBounds(): Boolean = x in bounds && y in bounds

        override fun toString(): String = grid.joinToString("\n") {
            it.joinToString("")
        }

        companion object {
            private const val TRAIL_START = 0
            private const val TRAIL_END = 9
        }
    }

    private data class Position(val x: Int, val y: Int) {

        fun translate(direction: Direction): Position = copy(
            x = x + direction.dx,
            y = y + direction.dy
        )

        override fun toString(): String = "($x, $y)"
    }

    private enum class Direction(val dx: Int, val dy: Int) {
        Up(0, -1),
        Right(1, 0),
        Down(0, 1),
        Left(-1, 0)
    }
}
