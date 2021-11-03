package com.example.sudoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudoku.ui.theme.SudokuTheme

val example = listOf<List<String>>(listOf("9","8","1","3","6","2","0","0","7"), listOf("3","0","0","1","6","7","2","5","4"), listOf("2","6","7","5","4","3","9","0","1"), listOf("9","8","1","3","6","0","0","0","0"), listOf("2","6","7","5","4","3","9","8","1"), listOf("3","9","0","1","6","7","0","0","4"), listOf("2","6","7","5","0","3","0","8","1"), listOf("3","9","8","1","6","7","2","5","4"), listOf("9","8","1","3","6","0","5","4","0"))
var change = ""

class PrimaryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudokuTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = Color.Black) {

                }
            }
        }
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
    val iv = hashSetOf<String>()

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
    val num = example[row][col]
    val number = remember{ mutableStateOf(if (num == "0") "" else num) }
    val fixed = remember{ mutableStateOf(num != "0") }
    val color = Color.LightGray

    Box(
        Modifier
            .height(40.dp)
            .width(40.dp)
            .padding(2.dp)
            .clip(
                RoundedCornerShape(2.dp)
            )
            .background(color)
            .clickable {
                if (!fixed.value) {
                    // Color Change
                    number.value = change
                }
            }
    ) {
        Text(text = number.value, Modifier.align(Alignment.Center),
            fontWeight = if(fixed.value)  FontWeight.Bold else FontWeight.Normal)
    }
}

// Number Pad
@Composable
fun DefNums() {
    Column(Modifier.padding(1.dp)) {
        Row {
            Dial( "1")
            Dial( "2")
            Dial( "3")
        }
        Row {
            Dial( "4")
            Dial( "5")
            Dial( "6")
        }
        Row {
            Dial( "7")
            Dial( "8")
            Dial("9")
        }
    }
}

@Composable
fun Dial(num: String = "") {
    val context = LocalContext.current


    Box(
        Modifier
            .height(60.dp)
            .width(60.dp)
            .padding(2.dp)
            .clip(
                RoundedCornerShape(5.dp)
            )
            .background(Color.White)
            .clickable { change = num }) {
        Text(text = num, Modifier.align(Alignment.Center))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {
            Grid()
            DefNums()
        }
    }
}