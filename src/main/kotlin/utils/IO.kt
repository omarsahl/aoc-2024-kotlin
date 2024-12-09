package com.omarsahl.utils

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

private fun inputFile(fileName: String): Path = Path("/Users/os/IdeaProjects/aoc24/src/main/resources/$fileName")

fun readFileInput(fileName: String): List<String> = inputFile(fileName).readLines()

fun readString(fileName: String): String = inputFile(fileName).readText()
