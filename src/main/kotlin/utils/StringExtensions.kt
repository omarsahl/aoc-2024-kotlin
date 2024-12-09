package com.omarsahl.utils

operator fun String.times(count: Int): String = buildString {
    repeat(count) {
        append(this@times)
    }
}