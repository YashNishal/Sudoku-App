package com.minimalsudoku.sudoku

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.delay
import java.io.IOException
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

fun initializeToasty() {
    Toasty.Config.getInstance()
//        .tintIcon(boolean tintIcon) // optional (apply textColor also to the icon)
//        .setTextSize(int sizeInSp) // optional
        .allowQueue( false) // optional (prevents several Toasts from queuing)
        .supportDarkTheme(true) // optional (whether to support dark theme or not)
//        .setRTL(boolean isRTL) // optional (icon is on the right)
        .apply() // required
}



@Throws(InterruptedException::class, IOException::class)
fun isConnected(): Boolean {
    val command = "ping -c 1 google.com"
    return Runtime.getRuntime().exec(command).waitFor() == 0
}