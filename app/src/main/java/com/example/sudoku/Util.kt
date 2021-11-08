package com.example.sudoku

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlin.math.abs

fun Array<IntArray>.copy() = Array(size) { get(it).clone() }

fun Path.standardQuadFromTo(from: Offset, to: Offset) {
    quadraticBezierTo(
        from.x,
        from.y,
        abs(from.x + to.x) /2f,
        abs(from.y + to.y) /2f
    )
}
