package com.omarsahl.puzzles.y2024

import com.omarsahl.utils.Log
import com.omarsahl.utils.readFileInput
import java.util.*

object Puzzle15 {

    // 1552879
    fun part1() {
        val (grid, moves) = parseInput()
        Warehouse(grid, false, moves)
            .apply(Warehouse::simulateRobotMoves)
            .sumGpsCoordinates()
            .also(::println)
    }

    // 1561175
    fun part2() {
        val (grid, moves) = parseInput()
        Warehouse(grid, true, moves)
            .apply(Warehouse::simulateRobotMoves)
            .sumGpsCoordinates()
            .also(::println)
    }

    private fun parseInput(): Pair<Array<CharArray>, List<Direction>> {
        val input = readFileInput("input15.txt")
        val separatorIndex = input.indexOf("")
        val gridInput = Array(separatorIndex) { input[it].toCharArray() }
        val directions = input.subList(separatorIndex, input.size).joinToString("").map(Direction::fromChar)
        return gridInput to directions
    }

    private class Warehouse(
        gridInput: Array<CharArray>,
        upgrade: Boolean,
        private val directions: List<Direction>
    ) {

        private val grid: Array<CharArray> = if (upgrade) upgradeWarehouse(gridInput) else gridInput
        private val boundsX = grid[0].indices
        private val boundsY = grid.indices
        private var robot = findRobot()

        init {
            Log.d { "Start\n\n${toString()}" }
        }

        fun simulateRobotMoves() {
            directions.forEach(::simulateMove)
        }

        fun sumGpsCoordinates(): Int {
            var sum = 0
            forEach { if (get(it).isBox()) sum += it.x + 100 * it.y }
            return sum
        }

        // ########
        // #...@OO#
        // ##..O..#
        // #...O..#
        // #.#.O..#
        // #...O..#
        // #......#
        // ########
        //
        // ##############
        // ##......##..##
        // ##..........##
        // ##...[][]...##
        // ##....[]....##
        // ##.....@....##
        // ##############
        private fun simulateMove(direction: Direction) {
            val next = robot + direction
            if (get(next) == Tile.Wall) {
                Log.d { "\nSkip $direction" }
                return
            }

            // If the next tile is empty, move the robot.
            if (get(next) == Tile.Empty) {
                moveRobot(direction)
                return
            }

            // If the next tile has a box, get all connected boxes and move them.
            val toMove = getMovableBoxes(robot, direction)
            if (toMove.isEmpty()) {
                Log.d { "\nSkip $direction" }
                return
            }

            for (i in toMove.lastIndex downTo 0) {
                val current = toMove[i]
                swap(current, current + direction.vec)
            }

            moveRobot(direction)

            Log.d { "\nMove $direction\n\n${toString()}" }
        }

        private fun getMovableBoxes(robot: Vec, direction: Direction): List<Vec> {
            val movable = mutableListOf<Vec>()
            val queue = LinkedList<Vec>().apply { add(robot + direction) }

            while (queue.isNotEmpty()) {
                val current = queue.poll()

                if (movable.contains(current)) continue else movable.add(current)

                if (get(current).isBoxPart()) {
                    queue.add(current.getOtherPart())
                }

                val next = current + direction
                when (get(next)) {
                    Tile.Wall -> return emptyList()
                    Tile.Box, Tile.BoxEnd, Tile.BoxStart -> queue.add(next)
                    Tile.Robot, Tile.Empty -> continue
                }
            }
            return movable
        }

        private fun get(vec: Vec): Tile = grid[vec.y][vec.x].let(Tile::fromChar)

        private fun set(vec: Vec, tile: Tile) {
            grid[vec.y][vec.x] = tile.char
        }

        private fun swap(a: Vec, b: Vec) {
            val t = get(a)
            set(a, get(b))
            set(b, t)
        }

        private inline fun forEach(action: (vec: Vec) -> Unit) {
            for (y in boundsY) {
                for (x in boundsX) {
                    action(Vec(x, y))
                }
            }
        }

        private operator fun contains(vec: Vec): Boolean = vec.x in boundsX && vec.y in boundsY

        private fun moveRobot(direction: Direction) {
            swap(robot, robot + direction)
            robot += direction
        }

        private fun findRobot(): Vec {
            forEach {
                if (get(it) == Tile.Robot) return it
            }
            error("Robot not found")
        }

        override fun toString(): String = grid.joinToString("\n") { it.joinToString("") }

        private enum class Tile(val char: Char) {
            Empty('.'),
            Wall('#'),
            Box('O'),
            BoxStart('['),
            BoxEnd(']'),
            Robot('@');

            companion object {
                private val tileMap = Tile.entries.associateBy { it.char }

                fun fromChar(char: Char): Tile = tileMap.getValue(char)
            }
        }

        private fun Tile.isBox(): Boolean = when (this) {
            Tile.Box, Tile.BoxStart -> true

            else -> false
        }

        private fun Tile.isBoxPart(): Boolean = when (this) {
            Tile.BoxStart, Tile.BoxEnd -> true
            else -> false
        }

        private fun Vec.getOtherPart(): Vec = when (get(this)) {
            Tile.BoxEnd -> plus(Direction.Left)
            Tile.BoxStart -> plus(Direction.Right)
            else -> error("Not a box")
        }

        companion object {

            private fun upgradeWarehouse(gridInput: Array<CharArray>): Array<CharArray> {
                return Array(gridInput.size) { y ->
                    val line = gridInput[y]
                    val arr = CharArray(line.size * 2)

                    for (x in line.indices) {
                        val i = x * 2

                        fun set(c1: Char, c2: Char) {
                            arr[i] = c1
                            arr[i + 1] = c2
                        }

                        when (val c = line[x]) {
                            '@' -> set(c, '.')
                            'O' -> set('[', ']')
                            else -> set(c, c)
                        }
                    }
                    arr
                }
            }
        }
    }

    private enum class Direction(val vec: Vec) {

        Up(Vec(0, -1)),
        Right(Vec(1, 0)),
        Down(Vec(0, 1)),
        Left(Vec(-1, 0));

        companion object {

            fun fromChar(char: Char): Direction = when (char) {
                '^' -> Up
                '>' -> Right
                'v' -> Down
                '<' -> Left
                else -> error("Invalid direction $char")
            }
        }
    }

    private data class Vec(val x: Int, val y: Int) {

        operator fun plus(other: Vec): Vec = copy(
            x = x + other.x,
            y = y + other.y
        )

        operator fun plus(direction: Direction): Vec = plus(direction.vec)

        operator fun minus(other: Vec): Vec = plus(other * -1)

        operator fun times(s: Int): Vec = copy(
            x = x * s,
            y = y * s
        )
    }
}
