package com.omarsahl

import kotlin.io.path.Path
import kotlin.io.path.readLines

fun readFileInput(fileName: String): List<String> =
    Path("/Users/os/IdeaProjects/aoc24/src/main/resources/$fileName").readLines()

fun main() {
}
