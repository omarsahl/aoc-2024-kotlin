package com.omarsahl.base

import java.nio.file.Path

abstract class Puzzle {

    abstract fun solvePart1(input: Path): Any

    abstract fun solvePart2(input: Path): Any
}
