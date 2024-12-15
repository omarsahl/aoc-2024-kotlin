package com.omarsahl.puzzles.y2024

import com.omarsahl.base.Puzzle
import java.nio.file.Path
import kotlin.io.path.readLines
import kotlin.math.pow

object Puzzle07 : Puzzle() {

    // 4555081946288
    override fun solvePart1(input: Path): Any {
        val lines = input.readLines()
        var sum = 0L
        for (equation in lines) {
            val result = equation.substringBefore(':').toLong()
            val operands = equation.substringAfter(": ").split(" ").map(String::toLong)
            if (hasSolution(result, operands)) sum += result
        }
        return sum
    }

    // 227921760109726
    override fun solvePart2(input: Path): Any {
        val lines = input.readLines()
        var sum = 0L
        for (equation in lines) {
            val result = equation.substringBefore(':').toLong()
            val operands = equation.substringAfter(": ").split(" ").map(String::toLong)
            if (hasSolutionWithConcatenation(result, operands)) sum += result
        }
        return sum
    }

    private fun hasSolution(target: Long, operands: List<Long>): Boolean {
        val opCount = operands.size - 1
        val combinations = 1 shl opCount // 2f.pow(opCount).toInt()
        repeat(combinations) { combination ->
            val binaryMask = BinaryMask(combination)
            val result = operands.reduceIndexed { i, acc, n ->
                if (acc > target) return@reduceIndexed acc
                binaryMask.opCode(i - 1).execute(acc, n)
            }

            if (result == target) return true
        }
        return false
    }

    private fun hasSolutionWithConcatenation(target: Long, operands: List<Long>): Boolean {
        val opCount = operands.size - 1
        val combinations = 3.0f.pow(opCount).toInt()
        repeat(combinations) { combination ->
            val ternaryMask = TernaryMask(combination, opCount)
            val result = operands.reduceIndexed { i, acc, n ->
                if (acc > target) return@reduceIndexed acc
                ternaryMask.opCode(i - 1).execute(acc, n)
            }

            if (result == target) return true
        }
        return false
    }

    @JvmInline
    private value class BinaryMask(private val mask: Int) {

        fun opCode(index: Int): OpCode = if (isBitSet(index)) OpCode.MUL else OpCode.ADD

        private fun isBitSet(index: Int): Boolean = (mask shr index and 1) == 1
    }

    private data class TernaryMask(private val mask: Int, private val opCount: Int) {

        private val opCodes: Array<OpCode> = run {
            var temp = mask
            Array(opCount) {
                val opIndex = temp % 3
                temp /= 3
                OpCode.from(opIndex)
            }
        }

        fun opCode(index: Int): OpCode = opCodes[index]
    }

    enum class OpCode {
        ADD {
            override fun execute(a: Long, b: Long): Long = a + b
        },
        MUL {
            override fun execute(a: Long, b: Long): Long = a * b
        },
        CONCAT {
            override fun execute(a: Long, b: Long): Long = "$a$b".toLong()
        };

        abstract fun execute(a: Long, b: Long): Long

        companion object {
            fun from(index: Int) = entries[index]
        }
    }
}
