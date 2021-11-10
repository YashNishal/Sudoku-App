package com.example.sudoku

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

class PrimaryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = resources.getColor(R.color.transparent)
        }




        initializeToasty()
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}


/* ---------------------MAIN APP----------------------- */


@Composable
fun App() {
    val solution = remember { mutableStateOf(false) }
    val correct = remember { mutableStateOf(false) }
    SudokuTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
            Background(mutableStateOf(true))
            Column {
                Spacer(modifier = Modifier.padding(12.dp))
                TopBar(solution)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (solution.value) {
                        matrix = original.copy()
                        Grid()
                    } else
                        Grid()
                    when {
                        correct.value -> {
                            FinalScreen(text = "VICTORY!")
                        }
                        solution.value -> {
                            FinalScreen(text = "TRY AGAIN")
                        }
                        else -> {
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
            .size(30.dp)
            .clickable {
                activity?.finish()
                customType(context, "fadein-to-fadeout")
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
                        .show();
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
    val rels = listOf(remember { mutableStateOf(false) },
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
            Dial("1", rels)
            Dial("2", rels)
            Dial("3", rels)
        }
        Row {
            Dial("4", rels)
            Dial("5", rels)
            Dial("6", rels)
        }
        Row {
            Dial("7", rels)
            Dial("8", rels)
            Dial("9", rels)
        }
    }
}


@Composable
fun Dial(num: String = "", rels: List<MutableState<Boolean>>) {
    Box(
        Modifier
            .height(60.dp)
            .width(60.dp)
            .padding(2.dp)

            .background(if (rels[num.toInt() - 1].value) CellHighlightColor else CellNormalColor)
            .clickable {
                change = num
                disableAll(rels)
                rels[num.toInt() - 1].value = true
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

@Composable
fun FinalScreen(text: String, color: Color = TextWhite) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
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

fun disableAll(rels: List<MutableState<Boolean>>) {
    for (i in 0..8) {
        rels[i].value = false
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    val solution = remember { mutableStateOf(true) }
    val correct = remember { mutableStateOf(false) }
    SudokuTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
            Background(mutableStateOf(true))
            Column {
                Spacer(modifier = Modifier.padding(12.dp))
                TopBar(solution)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (solution.value) {
                        matrix = original.copy()
                        Grid()
                    } else
                        Grid()
                    when {
                        correct.value -> {
                            FinalScreen(text = "VICTORY!", color = TextWhite)

                        }
                        solution.value -> {
                            FinalScreen(text = "TRY AGAIN", color = TextWhite)
                        }
                        else -> {
                            MiddleButtons(correct)
                            DialPad()
                        }
                    }
                }
            }
        }
    }
}