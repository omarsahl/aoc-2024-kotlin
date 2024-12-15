package com.omarsahl.puzzles.y2024

import com.omarsahl.utils.Log
import com.omarsahl.utils.isEven
import com.omarsahl.utils.readString

object Puzzle09 {

    // 6307275788409
    fun part1() {
        val input = readString("input9.txt")
        val diskMap = Disk(input).apply { compactWithFragmentation() }
        val checksum = diskMap.computeChecksum()
        println(checksum)
    }

    // 6327174563252
    fun part2() {
        val input = readString("input9.txt")
        val diskMap = Disk(input).apply { compactWithoutFragmentation() }
        val checksum = diskMap.computeChecksum()
        println(checksum)
    }

    private class Disk(input: String) {

        private val diskMap: MutableList<Int>

        private val lastIndex: Int
            get() = diskMap.lastIndex

        init {
            val diskMap = mutableListOf<Int>()
            var fileIdGen = 0
            input.forEachIndexed { i, c ->
                val fileId = if (i.isEven) fileIdGen++ else EMPTY
                repeat(c.digitToInt()) {
                    diskMap.add(fileId)
                }
            }
            this.diskMap = diskMap
            Log.d { toString() }
        }

        // 00...111...2...333.44.5555.6666.777.888899
        // 009..111...2...333.44.5555.6666.777.88889.
        // 0099.111...2...333.44.5555.6666.777.8888..
        // 00998111...2...333.44.5555.6666.777.888...
        // 009981118..2...333.44.5555.6666.777.88....
        // 0099811188.2...333.44.5555.6666.777.8.....
        // 009981118882...333.44.5555.6666.777.......
        // 0099811188827..333.44.5555.6666.77........
        // 00998111888277.333.44.5555.6666.7.........
        // 009981118882777333.44.5555.6666...........
        // 009981118882777333644.5555.666............
        // 00998111888277733364465555.66.............
        // 0099811188827773336446555566..............
        fun compactWithFragmentation() {
            var l = 0
            var r = lastIndex
            while (true) {
                while (diskMap[l] != EMPTY) l++
                while (diskMap[r] == EMPTY) r--
                if (l > r) break
                diskMap[l] = diskMap[r]
                diskMap[r] = EMPTY
                l++
                r--
            }
        }

        // 00...111...2...333.44.5555.6666.777.888899
        // 0099.111...2...333.44.5555.6666.777.8888..
        // 0099.1117772...333.44.5555.6666.....8888..
        // 0099.111777244.333....5555.6666.....8888..
        // 00992111777.44.333....5555.6666.....8888..
        fun compactWithoutFragmentation() {
            var r = lastIndex
            while (true) {
                while (diskMap[r] == EMPTY) r--

                val fileId = diskMap[r]
                var fileSize = 0
                while (diskMap[r] == fileId) {
                    fileSize++
                    if (r-- <= 0) return
                }

                var l = findNextEmptySpaceIndex(fileSize, r)
                if (l == -1) continue

                var rf = r + fileSize
                repeat(fileSize) {
                    diskMap[l++] = fileId
                    diskMap[rf--] = EMPTY
                }

                Log.d { toString() }
            }
        }

        fun computeChecksum(): Long {
            var checksum = 0L
            for (i in diskMap.indices) {
                val fileId = diskMap[i]
                if (fileId == EMPTY) continue
                checksum += i * fileId.toLong()
            }
            return checksum
        }

        private fun findNextEmptySpaceIndex(fileSize: Int, r: Int): Int {
            var l = 0
            while (l <= r) {
                var length = 0
                while (diskMap[l] != EMPTY && l <= r) l++
                while (diskMap[l] == EMPTY && l <= r) {
                    length++
                    l++
                }
                if (length >= fileSize) return l - length
            }
            return -1
        }

        override fun toString(): String = diskMap.joinToString("") {
            if (it == EMPTY) "." else ('0' + it).toString()
        }

        companion object {
            private const val EMPTY = -1
        }
    }
}
