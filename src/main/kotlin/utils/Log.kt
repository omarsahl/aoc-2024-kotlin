package com.omarsahl.utils

object Log {

    var logsEnabled = false

    inline fun d(message: () -> String) {
        if (logsEnabled) println(message())
    }

    inline fun e(message: () -> String) {
        if (logsEnabled) System.err.println(message())
    }
}