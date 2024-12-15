package com.omarsahl.puzzles.y2024

import com.omarsahl.base.Puzzle
import java.awt.image.BufferedImage
import java.awt.image.IndexColorModel
import java.io.File
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.createTempDirectory
import kotlin.io.path.readLines
import kotlin.math.floor

object Puzzle14 : Puzzle() {

    // 210587128
    override fun solvePart1(input: Path): Int =
        HighlySecuredEBHQ(input.readLines()).simulateRobotsMovement(100).calculateSafetyFactor()

    // Snapshot number 7286
    override fun solvePart2(input: Path): String {
        val imagesDir = HighlySecuredEBHQ(input.readLines()).snapshotRobotsMovement(10_000, 50)
        return "Snapshots directory: $imagesDir"
    }

    private class HighlySecuredEBHQ(
        private val dimensions: Dimensions,
        private val robots: List<Robot>
    ) {

        constructor(input: List<String>, dimensions: Dimensions = Dimensions.default) : this(
            dimensions,
            input.map(::parseRobot)
        )

        fun simulateRobotsMovement(seconds: Int): SimulationResult = SimulationResult(
            dimensions = dimensions,
            robots = robots.map { it.move(seconds, dimensions) }
        )

        fun snapshotRobotsMovement(maxSeconds: Int, snapshots: Int): Path {
            val visualizer = RobotsMovementVisualizer(dimensions.w, dimensions.h)
            (1..maxSeconds)
                .asSequence()
                .map {
                    val simulation = simulateRobotsMovement(it)
                    Triple(it, simulation.calculateSafetyFactor(), simulation.robots)
                }
                .sortedBy { it.second }
                .take(snapshots)
                .forEachIndexed { index, triple ->
                    visualizer.snapshot(triple.third, "${index + 1} - sec=${triple.first}, sf=${triple.second}")
                }
            return visualizer.outputDir
        }

        data class SimulationResult(
            private val dimensions: Dimensions,
            val robots: List<Robot>
        ) {

            fun calculateSafetyFactor(): Int {
                val (centerX, centerY) = dimensions.center()
                val quadrants = IntArray(4)
                for ((x, y) in robots) {
                    when {
                        x < centerX && y < centerY -> quadrants[0] = quadrants[0] + 1
                        x > centerX && y < centerY -> quadrants[1] = quadrants[1] + 1
                        x < centerX && y > centerY -> quadrants[2] = quadrants[2] + 1
                        x > centerX && y > centerY -> quadrants[3] = quadrants[3] + 1
                        else -> continue
                    }
                }
                return quadrants.reduce { acc, q -> acc * q }
            }
        }

        companion object {

            private fun parseRobot(input: String): Robot {
                val (p, v) = input.split(" ")
                val (x, y) = p.substring(2).split(",").map(String::toInt)
                val (vx, vy) = v.substring(2).split(",").map(String::toInt)
                return Robot(x, y, vx, vy)
            }
        }
    }

    private data class Robot(val x: Int, val y: Int, val vx: Int, val vy: Int) {

        fun move(seconds: Int, dimensions: Dimensions): Robot {
            val xl = teleport(x + vx * seconds, dimensions.w)
            val yl = teleport(y + vy * seconds, dimensions.h)
            return copy(x = xl, y = yl)
        }

        private fun teleport(v: Int, max: Int): Int = (v % max).let { if (it < 0) it + max else it }
    }

    private data class Dimensions(val w: Int, val h: Int) {

        fun center(): Dimensions = Dimensions(
            w = floor(w / 2f).toInt(),
            h = floor(h / 2f).toInt()
        )

        companion object {
            val default = Dimensions(101, 103)
        }
    }

    private class RobotsMovementVisualizer(
        private val w: Int,
        private val h: Int
    ) {
        private val colorMap = byteArrayOf(0, 255.toByte())
        private val colorModel = IndexColorModel(2, colorMap.size, colorMap, colorMap, colorMap)
        private val raster = colorModel.createCompatibleWritableRaster(w, h)

        val outputDir = createTempDirectory(prefix = "aoc-puzzle14-")

        fun snapshot(robots: List<Robot>, imageName: String) {
            val imageData = IntArray(w * h) { 0 }
            robots.forEach { imageData[it.y * w + it.x] = 1 }

            raster.setPixels(0, 0, w, h, imageData)
            BufferedImage(colorModel, raster, false, null).writeJpg(imageName, outputDir)
        }

        fun BufferedImage.writeJpg(name: String, outputDir: Path) {
            ImageIO.write(this, "png", File(outputDir.toFile(), "$name.png"))
        }
    }
}
