package com.omarsahl.puzzles

import com.omarsahl.readFileInput
import kotlin.math.abs

object Puzzle1 {

    fun part1() {
        val input = readFileInput("input1.txt")

        val a1 = IntArray(input.size)
        val a2 = IntArray(input.size)
        input.forEachIndexed { index, str ->
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

        println(sum)
    }

    fun part2() {
        val input = readFileInput("input1.txt")

        val a1 = IntArray(input.size)
        val lookup = mutableMapOf<Int, Int>()
        input.forEachIndexed { index, str ->
            a1[index] = str.substringBefore(" ").toInt()
            val x = str.substringAfterLast(" ").toInt()
            val count = lookup.getOrPut(x) { 0 }
            lookup[x] = count + 1
        }

        a1.fold(0) { acc, x -> acc + x * lookup.getOrDefault(x, 0) }.also(::println)
    }
}
