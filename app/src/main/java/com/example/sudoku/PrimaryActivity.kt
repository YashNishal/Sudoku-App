package com.example.sudoku

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudoku.ui.theme.*
import es.dmoral.toasty.Toasty
import maes.tech.intentanim.CustomIntent.customType

var change = "0"

@ExperimentalComposeUiApi
class PrimaryActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {

        @Suppress("DEPRECATION")
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = resources.getColor(R.color.transparent)
        }

        initializeToasty()
        super.onCreate(savedInstanceState)
        setContent {
            val solution = remember { mutableStateOf(false) }
            val correct = remember { mutableStateOf(false) }
            App(solution, correct)
        }
    }
}


/* ---------------------MAIN APP----------------------- */


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun App(solution: MutableState<Boolean>, correct: MutableState<Boolean>) {
    SudokuTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
            Background(mutableStateOf(true))
            Column {
                Spacer(modifier = Modifier.padding(12.dp))
                TopBar(solution)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (solution.value) {
                        matrix = original.copy()
                        Grid()
                    } else
                        Grid()


                    AnimatedVisibility(
                        visible = correct.value,
                        enter = fadeIn(animationSpec = tween(500)),
                    ) {
                        FinalScreen(text = "VICTORY!")
                    }
                    AnimatedVisibility(
                        visible = solution.value,
                        enter = fadeIn(animationSpec = tween(500))
                    ) {
                        FinalScreen(text = "TRY AGAIN")
                    }

                    AnimatedVisibility(
                        visible = (!correct.value && !solution.value),
                        exit = fadeOut(animationSpec = tween(500))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            MiddleButtons(correct)
                            DialPad()
                        }
                    }
                }
            }
        }
    }
}

/* ------------------------------------------------------------------- */
// COMPOSABLE FUNCTIONS


@ExperimentalComposeUiApi
@Composable
fun TopBar(solution: MutableState<Boolean>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        BackButton(30)
        SolutionButton(solution)
    }
}

@ExperimentalComposeUiApi
@Composable
fun BackButton(size: Int) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    Icon(
        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_24),
        contentDescription = "Back",
        tint = Color.White,
        modifier = Modifier
            .padding(15.dp)
            .size(size.dp)
            .pointerInteropFilter {
                if (it.action == ACTION_UP) {
                    activity?.finish()
                    customType(context, "fadein-to-fadeout")
                }
                true
            }
    )
}

@Composable
fun SolutionButton(solution: MutableState<Boolean>) {
    val context = LocalContext.current

    Text(
        text = "SOLUTION",
        fontSize = 25.sp,
        color = TextWhite,
        fontWeight = FontWeight.Light,
        letterSpacing = 5.sp,
        modifier = Modifier
            .padding(15.dp)
            .clickable {
                Log.d("Before getSolution : ", original.contentDeepToString())
                if (!getSolution()) {
                    Toasty
                        .error(context, "Unable to get Solution", Toast.LENGTH_SHORT, true)
                        .show()
                }
                Log.d("After getSolution : ", original.contentDeepToString())
                solution.value = true
            }
    )
}


/* ---------------------- */
// GRID
// Sudoku Grid and Cells


@Composable
fun Grid() {
    Column(Modifier.padding(2.dp)) {
        Row {
            SubGrid(0)
            SubGrid(1)
            SubGrid(2)
        }
        Row {
            SubGrid(3)
            SubGrid(4)
            SubGrid(5)
        }
        Row {
            SubGrid(6)
            SubGrid(7)
            SubGrid(8)
        }
    }
}


@Composable
fun SubGrid(i: Int) {
    val row: Int = (i / 3) * 3
    val col: Int = (i % 3) * 3
    Column(Modifier.padding(1.dp)) {
        Row {
            Cell(row, col)
            Cell(row, col + 1)
            Cell(row, col + 2)
        }
        Row {
            Cell(row + 1, col)
            Cell(row + 1, col + 1)
            Cell(row + 1, col + 2)
        }
        Row {
            Cell(row + 2, col)
            Cell(row + 2, col + 1)
            Cell(row + 2, col + 2)
        }
    }
}


@Composable
fun Cell(row: Int, col: Int) {
    val num = matrix[row][col].toString()
    val number = remember { mutableStateOf(if (num == "0") "" else num) }
    val fixed = remember { mutableStateOf(num != "0") }

    Box(
        Modifier
            .height(40.dp)
            .width(40.dp)
            .padding(0.75.dp)
//            .clip(
//                RoundedCornerShape(2.dp)
//            )
            .background(if (fixed.value) CellHighlightColor else CellNormalColor)
            .clickable {
                if (!fixed.value) {
                    // Color Change
                    number.value = change
                    matrix[row][col] = change.toInt()
                }
            }
    ) {
        Text(
            text = if (number.value == "0") "" else number.value,
            Modifier.align(Alignment.Center),
            fontSize = 24.sp,
            fontWeight = if (fixed.value) FontWeight.Normal else FontWeight.Light
        )
    }
}


/* ---------------------- */
// MIDDLE BUTTONS

@Composable
fun MiddleButtons(correct: MutableState<Boolean>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
    ) {
        CheckButton(correct)
        EraseButton()
    }
}


@Composable
fun CheckButton(correct: MutableState<Boolean>) {
    val context = LocalContext.current
    Text(
        modifier = Modifier
            .padding(15.dp)

            .background(CellNormalColor)
            .clickable {
                if (check()) {
                    var noZero = true
                    for (i in 0..8)
                        for (j in 0..8)
                            if (matrix[i][j] == 0) {
                                noZero = false
                                break
                            }
                    if (noZero)
                        correct.value = true

                    Toasty
                        .success(context, "Correct!", Toast.LENGTH_SHORT, true)
                        .show()
                } else
                    Toasty
                        .error(context, "Incorrect.", Toast.LENGTH_SHORT, true)
                        .show()
            }
            .padding(6.dp),
        text = "CHECK",
        fontSize = 22.sp,
        color = Color.Black,
        fontWeight = FontWeight.Light,
        letterSpacing = 5.sp,

        )
}


@Composable
fun EraseButton() {
//    Box(
//        modifier = Modifier.padding(15.dp),
//        // for other style
//    ) {
//        Icon(painter = painterResource(id = R.drawable.ic_icons8_erase_5),
//            contentDescription = "Erase",
//            tint = Color.White,
//            modifier = Modifier
//                .clickable {
//                    change = "0"
//                }
//                .size(40.dp)
//        )
//    }
    Text(
        modifier = Modifier
            .padding(15.dp)

            .background(CellNormalColor)
            .clickable {
                change = "0"
            }
            .padding(6.dp),
        text = "ERASE",
        fontSize = 22.sp,
        color = Color.Black,
        fontWeight = FontWeight.Light,
        letterSpacing = 5.sp,

        )
}


/* ---------------------- */
// DIAL PAD
@Composable
fun DialPad() {
    val dialStateList = listOf(remember { mutableStateOf(false) },
        remember { mutableStateOf(false) },
        remember { mutableStateOf(false) },
        remember { mutableStateOf(false) },
        remember { mutableStateOf(false) },
        remember { mutableStateOf(false) },
        remember { mutableStateOf(false) },
        remember { mutableStateOf(false) },
        remember { mutableStateOf(false) })
    Column(
        Modifier
            .padding(1.dp)
            .clickable { }) {
        Row {
            Dial("1", dialStateList)
            Dial("2", dialStateList)
            Dial("3", dialStateList)
        }
        Row {
            Dial("4", dialStateList)
            Dial("5", dialStateList)
            Dial("6", dialStateList)
        }
        Row {
            Dial("7", dialStateList)
            Dial("8", dialStateList)
            Dial("9", dialStateList)
        }
    }
}


@Composable
fun Dial(num: String = "", dialStateList: List<MutableState<Boolean>>) {
    Box(
        Modifier
            .height(60.dp)
            .width(60.dp)
            .padding(2.dp)

            .background(if (dialStateList[num.toInt() - 1].value) CellHighlightColor else CellNormalColor)
            .clickable {
                change = num
                disableAll(dialStateList)
                dialStateList[num.toInt() - 1].value = true
            }) {
        Text(
            text = num,
            Modifier
                .align(Alignment.Center),
            fontSize = 26.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal
        )
    }
}


/* ---------------------- */
// FINAL SCREEN

@ExperimentalComposeUiApi
@Composable
fun FinalScreen(text: String, color: Color = TextWhite) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Spacer(modifier = Modifier.padding(top = 100.dp))
        Text(
            text = text,
            fontSize = 50.sp,
            color = color,
            fontWeight = FontWeight.Light,
            letterSpacing = 10.sp,
            textAlign = TextAlign.Center
        )
        BackButton(60)
    }
}


/* ---------------------------------------------------------------------- */
// HELPER FUNCTIONS

fun disableAll(dialStateList: List<MutableState<Boolean>>) {
    for (i in 0..8) {
        dialStateList[i].value = false
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun PrimaryScreen() {
    val solution = remember { mutableStateOf(false) }
    val correct = remember { mutableStateOf(false) }
    App(solution, correct)
}


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun TryAgainScreen() {
    val solution = remember { mutableStateOf(true) }
    val correct = remember { mutableStateOf(false) }
    App(solution, correct)
}


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun VictoryScreen() {
    val solution = remember { mutableStateOf(false) }
    val correct = remember { mutableStateOf(true) }
    App(solution, correct)
}