package com.example.sudoku

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudoku.ui.theme.BrightBlue
import com.example.sudoku.ui.theme.SudokuTheme
import com.example.sudoku.ui.theme.TextWhite
import java.util.Arrays.copyOf

var change = "1"

class PrimaryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    val solution = remember{ mutableStateOf(false)}
    val correct = remember{ mutableStateOf(false)}
    SudokuTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = Color.Black,modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {
                if (solution.value) {
                    matrix = original.copy()
                    Grid()
                }
                else
                    Grid()
                when {
                    correct.value -> {
                        FinalScreen(text = "Victory!", color = Color.Green)
                    }
                    solution.value -> {
                        FinalScreen(text = "GAME OVER!", color = Color.Red)
                    }
                    else -> {
                        MiddleButtons(solution, correct)
                        DefNums()
                    }
                }
            }
        }
    }
}
@Composable
fun BackButton() {
    val activity = (LocalContext.current as? Activity)
    Box(contentAlignment = Alignment.TopStart , modifier = Modifier.fillMaxWidth()) {
        Icon(painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back",tint = Color.White,
            modifier = Modifier
                .padding(15.dp)
                .clip(RoundedCornerShape(100))
                .background(BrightBlue)
                .padding(15.dp)
                .clickable {

                    activity?.finish()
                }
        )
    }
}
// Kuch hua kya
fun Array<IntArray>.copy() = Array(size) { get(it).clone() }

@Composable
fun FinalScreen(text: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {
        Text(text=text, fontSize = 70.sp, color = color, textAlign = TextAlign.Center)
//        BackButton()
    }
}

@Composable
fun MiddleButtons(solution: MutableState<Boolean>, correct: MutableState<Boolean>) {
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,modifier = Modifier.fillMaxWidth()
    ) {
        ValidateButton(correct)
        SolveButton(solution)
    }
}

@Composable
fun ValidateButton(correct: MutableState<Boolean>) {
    val context = LocalContext.current
    Box(modifier = Modifier
        .padding(15.dp)
        .height(50.dp)
        .width(70.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(BrightBlue)
        .clickable {
            if (check()) {
                var noZero = true
                for(i in 0..8)
                    for(j in 0..8)
                        if(matrix[i][j] == 0 ) {
                            noZero = false
                            break;
                        }
                if(noZero)
                    correct.value = true

                Toast
                    .makeText(context, "Correct!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast
                    .makeText(context, "Incorrect.", Toast.LENGTH_SHORT)
                    .show()
            }
        }) {
        Text(text = "Validate",
            color = TextWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun SolveButton(solution: MutableState<Boolean>) {
    val context = LocalContext.current
    Box(modifier = Modifier
        .padding(15.dp)
        .height(50.dp)
        .width(70.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(BrightBlue)
        .clickable {
            Log.d("Before getSolution : ", original.contentDeepToString())
            if (getSolution()) {
                Toast
                    .makeText(context, "got the solution!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast
                    .makeText(context, "unable to get the solution!.", Toast.LENGTH_SHORT)
                    .show()
            }
            Log.d("After getSolution : ", original.contentDeepToString())
            solution.value = true
        }) {
        Text(text = "Solution",
            color = TextWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

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
fun SubGrid(i:Int) {
    val row : Int = (i/3)*3
    val col : Int = (i%3)*3
    Column(Modifier.padding(1.dp)) {
        Row {
            Cell(row, col)
            Cell(row, col+1)
            Cell(row, col+2)
        }
        Row {
            Cell(row+1,col)
            Cell(row+1,col+1)
            Cell(row+1,col+2)
        }
        Row {
            Cell(row+2,col)
            Cell(row+2,col+1)
            Cell(row+2,col+2)
        }
    }
}

@Composable
fun Cell(row: Int, col: Int) {
    val context = LocalContext.current
    val num = matrix[row][col].toString()
    val number = remember{ mutableStateOf(if (num == "0") "" else num) }
    val fixed = remember{ mutableStateOf(num != "0") }

    Box(
        Modifier
            .height(40.dp)
            .width(40.dp)
            .padding(2.dp)
            .clip(
                RoundedCornerShape(2.dp)
            )
            .background(if (fixed.value) Color.LightGray else Color.White)
            .clickable {
                if (!fixed.value) {
                    // Color Change
                    number.value = change
                    matrix[row][col] = change.toInt()
                }
            }
    ) {
        Text(text = number.value, Modifier.align(Alignment.Center),
            fontWeight = if(fixed.value)  FontWeight.ExtraBold else FontWeight.Light)
    }
}

// Number Pad
@Composable
fun DefNums() {
    val rels = listOf(remember { mutableStateOf(true) },remember { mutableStateOf(false) },remember { mutableStateOf(false) },remember { mutableStateOf(false) },remember { mutableStateOf(false) },remember { mutableStateOf(false) },remember { mutableStateOf(false) },remember { mutableStateOf(false) },remember { mutableStateOf(false) })
    Column(
        Modifier
            .padding(1.dp)
            .clickable { }) {
        Row {
            Dial( "1", rels)
            Dial( "2", rels)
            Dial( "3", rels)
        }
        Row {
            Dial( "4", rels)
            Dial( "5", rels)
            Dial( "6", rels)
        }
        Row {
            Dial( "7", rels)
            Dial( "8", rels)
            Dial( "9", rels)
        }
    }
}

@Composable
fun Dial(num: String = "", rels: List<MutableState<Boolean>>) {
    val context = LocalContext.current
    Box(
        Modifier
            .height(60.dp)
            .width(60.dp)
            .padding(2.dp)
            .clip(
                RoundedCornerShape(5.dp)
            )
            .background(if (rels[num.toInt() - 1].value) Color.LightGray else Color.White)
            .clickable {
                change = num
                disableAll(rels)
                rels[num.toInt() - 1].value = true
            }) {
        Text(text = num, Modifier.align(Alignment.Center))
    }
}

fun disableAll(rels: List<MutableState<Boolean>>) {
    for (i in 0..8) {
        rels[i].value = false
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    App()
}