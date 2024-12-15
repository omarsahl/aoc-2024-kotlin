package com.omarsahl.puzzles.y2024

import com.omarsahl.utils.readFileInput

object Puzzle06 {

    fun part1() {
        val input = readFileInput("input6.txt")
        LabGrid(input).findGuardVisitedTilesCount().also(::println)
    }

    fun part2() {
        val input = readFileInput("input6.txt")
        LabGrid(input).findPossibleObstacles().also(::println)
    }


    private enum class Direction(val dx: Int, val dy: Int) {
        Up(0, -1),
        Right(1, 0),
        Down(0, 1),
        Left(-1, 0);

        val size: Int
            get() = entries.size
    }

    private fun Direction.turnRight() = Direction.entries[(ordinal + 1) % size]

    private data class Position(val x: Int, val y: Int) {

        override fun toString(): String = "($x, $y)"

        fun isAt(x: Int, y: Int): Boolean = this.x == x && this.y == y
    }

    private fun Position.applyDirection(direction: Direction): Position = copy(
        x = x + direction.dx,
        y = y + direction.dy
    )

    private enum class Tile(val char: Char) {
        Empty('.'),
        Guard('^'),
        Obstacle('#'),
        OutOfBounds(Char.MIN_VALUE);
    }

    private fun Char?.toTile() = when (this) {
        Tile.Empty.char -> Tile.Empty
        Tile.Guard.char -> Tile.Guard
        Tile.Obstacle.char -> Tile.Obstacle
        else -> Tile.OutOfBounds
    }

    private class LabGrid(private val input: List<String>) {

        private val grid: Array<CharArray>
        private val guardPosition: Position
        private val initialDirection = Direction.Up

        init {
            var guardPosition: Position? = null
            grid = Array(input.size) { y ->
                val row = input[y].toCharArray()
                if (guardPosition == null) {
                    val guardX = row.indexOf(Tile.Guard.char)
                    if (guardX != -1) {
                        guardPosition = Position(guardX, y)
                    }
                }
                row
            }
            this.guardPosition = checkNotNull(guardPosition)
        }

        fun findGuardVisitedTilesCount(): Int {
            var direction = initialDirection
            var pos = guardPosition
            val visited = mutableSetOf(pos)
            while (true) {
                val tileAhead = getTileAt(pos.x + direction.dx, pos.y + direction.dy)
                when (tileAhead) {
                    Tile.OutOfBounds -> break
                    Tile.Obstacle -> direction = direction.turnRight()
                    Tile.Empty,
                    Tile.Guard -> {
                        pos = pos.applyDirection(direction)
                        visited.add(pos)
                    }
                }
            }
            println("$pos $direction")
            return visited.size
        }

        fun findPossibleObstacles(): Int {
            var result = 0
            forEachTile { x, y ->
                if (guardPosition.isAt(x, y)) {
                    return@forEachTile
                }

                if (isEmptyAt(x, y)) {
                    withTemporaryObstacle(x, y) {
                        if (isLoopingPath()) result++
                    }
                }
            }
            return result
        }

        private fun isLoopingPath(): Boolean {
            var direction = initialDirection
            var pos = guardPosition
            val visited = hashSetOf<Pair<Position, Direction>>()
            while (true) {
                val tileAhead = getTileAt(pos.x + direction.dx, pos.y + direction.dy)
                when (tileAhead) {
                    Tile.OutOfBounds -> return false
                    Tile.Obstacle -> direction = direction.turnRight()

                    Tile.Empty,
                    Tile.Guard -> {
                        pos = pos.applyDirection(direction)
                        if (!visited.add(pos to direction)) return true
                    }
                }
            }
        }

        private inline fun forEachTile(action: (x: Int, y: Int) -> Unit) {
            for (y in grid.indices) {
                for (x in grid[y].indices) {
                    action(x, y)
                }
            }
        }

        private inline fun withTemporaryObstacle(x: Int, y: Int, block: () -> Unit) {
            setTileAt(x, y, Tile.Obstacle)
            block()
            setTileAt(x, y, Tile.Empty)
        }


        private fun getTileAt(x: Int, y: Int): Tile = grid.getOrNull(y)?.getOrNull(x).toTile()

        private fun setTileAt(x: Int, y: Int, tile: Tile) {
            grid[y][x] = tile.char
        }

        private fun isEmptyAt(x: Int, y: Int): Boolean = getTileAt(x, y) == Tile.Empty
    }
}
