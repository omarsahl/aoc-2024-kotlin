package com.omarsahl.puzzles.y2024

import com.omarsahl.base.Puzzle
import java.nio.file.Path
import kotlin.io.path.readLines

object Puzzle08 : Puzzle() {

    // 276
    override fun solvePart1(input: Path): Any {
        val lines = input.readLines()
        return CityMap(lines).findAllAntinodes()
    }

    // 991
    override fun solvePart2(input: Path): Any {
        val lines = input.readLines()
        return CityMap(lines).findAllAntinodesWithResonantHarmonics()
    }

    private class CityMap(val input: List<String>) {

        private val bounds: IntRange
        private val grid: Array<CharArray>
        private val antennasMap: Map<Frequency, List<Location>>

        init {
            val antennas = mutableMapOf<Frequency, MutableList<Location>>()
            this.grid = Array(input.size) { y ->
                val row = input[y].toCharArray()
                row.forEachIndexed { x, frequency ->
                    if (frequency != EMPTY) {
                        val antenna = Location(x, y)
                        antennas.getOrPut(frequency) { mutableListOf() }.add(antenna)
                    }
                }
                row
            }
            this.bounds = grid.indices
            this.antennasMap = antennas
        }

        fun findAllAntinodes(): Int {
            val antinodes = hashSetOf<Location>()
            forEachAntennasPair { antenna1, antenna2 ->
                antenna1.antinodeOppositeTo(antenna2).also {
                    if (it.withinBounds()) antinodes.add(it)
                }

                antenna2.antinodeOppositeTo(antenna1).also {
                    if (it.withinBounds()) antinodes.add(it)
                }
            }
            return antinodes.size
        }

        fun findAllAntinodesWithResonantHarmonics(): Int {
            val antinodes = hashSetOf<Location>()
            forEachAntennasPair { antenna1, antenna2 ->
                var dx = antenna2.x - antenna1.x
                var dy = antenna2.y - antenna1.y
                var antinode = antenna1
                while (antinode.withinBounds()) {
                    antinodes.add(antinode)
                    antinode = antinode.plus(dx, dy)
                }

                dx *= -1
                dy *= -1
                antinode = antenna2
                while (antinode.withinBounds()) {
                    antinodes.add(antinode)
                    antinode = antinode.plus(dx, dy)
                }
            }

            return antinodes.size
        }

        private fun Location.withinBounds(): Boolean = x in bounds && y in bounds

        private inline fun forEachAntennasPair(action: (antenna1: Location, antenna2: Location) -> Unit) {
            for ((_, antennas) in antennasMap) {
                if (antennas.size < 2) continue

                for (i in antennas.indices) {
                    for (j in i + 1..antennas.lastIndex) {
                        action(antennas[i], antennas[j])
                    }
                }
            }
        }
    }

    private data class Location(val x: Int, val y: Int) {

        fun plus(dx: Int, dy: Int): Location = copy(x = x + dx, y = y + dy)

        fun antinodeOppositeTo(other: Location): Location {
            val dx = x - other.x
            val dy = y - other.y
            return Location(x + dx, y + dy)
        }
    }

    private const val EMPTY = '.'
}

private typealias Frequency = Char
