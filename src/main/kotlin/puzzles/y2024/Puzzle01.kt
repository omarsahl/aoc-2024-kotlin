package com.omarsahl.puzzles.y2024

import com.omarsahl.base.Puzzle
import java.nio.file.Path
import kotlin.io.path.readLines
import kotlin.math.abs

object Puzzle01 : Puzzle() {

    override fun solvePart1(input: Path): Any {
        val lines = input.readLines()
        val a1 = IntArray(lines.size)
        val a2 = IntArray(lines.size)

        lines.forEachIndexed { index, str ->
            a1[index] = str.substringBefore(" ").toInt()
            a2[index] = str.substringAfterLast(" ").toInt()
        }
        assert(a1.size == a2.size)

        a1.sort()
        a2.sort()

        var sum = 0
        for (i in a1.indices) {
            sum += abs(a1[i] - a2[i])
        }

        return sum
    }

    override fun solvePart2(input: Path): Any {
        val lines = input.readLines()
        val a1 = IntArray(lines.size)
        val lookup = mutableMapOf<Int, Int>()

        lines.forEachIndexed { index, str ->
            a1[index] = str.substringBefore(" ").toInt()
            val x = str.substringAfterLast(" ").toInt()
            val count = lookup.getOrPut(x) { 0 }
            lookup[x] = count + 1
        }

        return a1.fold(0) { acc, x -> acc + x * lookup.getOrDefault(x, 0) }
    }
}
