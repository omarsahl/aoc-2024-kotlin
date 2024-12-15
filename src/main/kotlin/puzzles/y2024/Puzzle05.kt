package com.omarsahl.puzzles.y2024

import com.omarsahl.base.Puzzle
import java.nio.file.Path
import kotlin.io.path.readLines

object Puzzle05 : Puzzle() {

    private class Part1(input: List<String>) : AbstractPart(input) {

        override fun getMiddlePage(pages: List<Int>, indicesMap: Map<Int, Int>): Int {
            if (isSorted(indicesMap)) {
                return pages[pages.size / 2]
            }
            return 0
        }
    }

    private class Part2(input: List<String>) : AbstractPart(input) {

        override fun getMiddlePage(pages: List<Int>, indicesMap: Map<Int, Int>): Int {
            if (!isSorted(indicesMap)) {
                val sorted = sort(pages, indicesMap)
                return sorted[sorted.size / 2]
            }
            return 0
        }

        private fun sort(pages: List<Int>, indicesMap: Map<Int, Int>): List<Int> {
            val sortingMap = hashMapOf<Int, Int>()
            for ((s, e) in rules) {
                if (s in indicesMap && e in indicesMap) {
                    sortingMap[s] = (sortingMap[s] ?: 0) + 1
                }
            }
            println("Sorting map: $sortingMap")

            return pages.sortedByDescending { sortingMap[it] }.also {
                println("Sorted pages: $it")
            }
        }
    }

    private abstract class AbstractPart(private val input: List<String>) {

        protected val rules = mutableListOf<Pair<Int, Int>>()

        fun run(): Int {
            var index = 0
            while (index < input.size) {
                val line = input[index]
                if (line.isEmpty()) {
                    break
                }

                val separatorIndex = line.indexOf('|')
                val x = line.substring(0, separatorIndex).toInt()
                val y = line.substring(separatorIndex + 1).toInt()
                rules.add(x to y)
                index++
            }
            println("Rules size: ${rules.size}")
            index++

            var result = 0
            while (index < input.size) {
                val line = input[index]
                val pages = line.split(',').map(String::toInt)
                val indicesMap = mapToIndices(pages)
                result += getMiddlePage(pages, indicesMap)
                index++
            }

            return result
        }

        protected abstract fun getMiddlePage(pages: List<Int>, indicesMap: Map<Int, Int>): Int

        protected fun isSorted(indicesMap: Map<Int, Int>): Boolean {
            for ((s, e) in rules) {
                if (s in indicesMap && e in indicesMap && indicesMap[s]!! >= indicesMap[e]!!) {
                    return false
                }
            }
            return true
        }

        private fun mapToIndices(pages: List<Int>): Map<Int, Int> {
            val pagesMap = linkedMapOf<Int, Int>()
            for (i in pages.indices) {
                pagesMap[pages[i]] = i
            }
            return pagesMap
        }
    }

    override fun solvePart1(input: Path): Any {
        val lines = input.readLines()
        return Part1(lines).run()
    }

    override fun solvePart2(input: Path): Any {
        val lines = input.readLines()
        return Part2(lines).run()
    }
}
