package com.omarsahl.utils

val Int.isEven: Boolean
    get() = (this and 1) == 0
