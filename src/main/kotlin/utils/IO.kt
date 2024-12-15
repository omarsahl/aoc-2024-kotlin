package com.omarsahl.utils

import java.nio.file.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.io.path.toPath

private object IO

private val classLoader: ClassLoader get() = IO::class.java.classLoader

private fun inputFile(fileName: String): Path = classLoader.getResource(fileName)!!.toURI().toPath()

fun readFileInput(fileName: String): List<String> = inputFile(fileName).readLines()

fun readString(fileName: String): String = inputFile(fileName).readText()
