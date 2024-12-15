package com.omarsahl.utils

import java.nio.file.Path
import kotlin.io.path.toPath

private object IO

private val classLoader: ClassLoader get() = IO::class.java.classLoader

fun getPuzzleRealInputFile(year: Int, day: Int): Path =
    classLoader.getResource("inputs/y$year/real/input$day.txt")!!.toURI().toPath()
