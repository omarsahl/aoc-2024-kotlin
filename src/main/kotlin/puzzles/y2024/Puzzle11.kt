package com.omarsahl.puzzles.y2024

import com.omarsahl.utils.isEven
import com.omarsahl.utils.readString

object Puzzle11 {

    fun part1() {
        blink(readStones(), 25).also(::println)
    }

    fun part2() {
        blink(readStones(), 75).also(::println)
    }

    private val cache = HashMap<Key, Long>()

    private fun blink(stones: List<Stone>, times: Int): Long = stones.sumOf { evolve(it, times) }

    private fun evolve(stone: Stone, times: Int): Long {
        if (times == 0) return 1
        val key = Key(stone, times)
        return cache.getOrPut(key) {
            stone.evolve().sumOf {
                evolve(it, times - 1)
            }
        }
    }

    private fun readStones(): List<Stone> = readString("input11.txt").split(" ").map(::Stone)
}

private typealias Key = Pair<Stone, Int>

@JvmInline
private value class Stone(private val value: Long) {

    constructor(str: String) : this(str.toLong())

    fun evolve(): Array<Stone> = when {
        value == 0L -> arrayOf(Stone(1))
        "$value".length.isEven -> split()
        else -> arrayOf(Stone(value * 2024))
    }

    private fun split(): Array<Stone> {
        val str = "$value"
        return arrayOf(
            Stone(str.substring(0, str.length / 2).toLong()),
            Stone(str.substring(str.length / 2).toLong()),
        )
    }
}
