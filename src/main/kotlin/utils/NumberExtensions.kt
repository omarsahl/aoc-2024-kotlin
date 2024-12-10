package com.omarsahl.utils

val Int.isEven: Boolean
    get() = (this and 1) == 0

fun Long.isBitSet(index: Int): Boolean = (this shr index and 1) == 1L

fun Long.setBit(index: Int) = this or (1L shl index)
