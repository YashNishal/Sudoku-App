package com.example.sudoku

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlinx.coroutines.delay
import kotlin.math.abs

// Matrix Deep Copy
fun Array<IntArray>.copy() = Array(size) { get(it).clone() }

// Bezier Curves
fun Path.standardQuadFromTo(from: Offset, to: Offset) {
    quadraticBezierTo(
        from.x,
        from.y,
        abs(from.x + to.x) /2f,
        abs(from.y + to.y) /2f
    )
}

// Delay Function
suspend fun stateChange(c: MutableState<Boolean>) {
    while (true) {
        c.value = !c.value
        delay(4000)
    }
}
