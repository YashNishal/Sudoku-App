package com.example.sudoku

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudoku.ui.theme.Orange
import com.example.sudoku.ui.theme.SudokuTheme
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudokuTheme {
                // A surface container using the 'background' color from the theme
                SudokuApp()
            }
        }
    }
}

@Composable
fun SudokuApp() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
        Buttons()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun Buttons() {
    val context = LocalContext.current;
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
        Button(onClick = { onClick("easy", context) }, colors = ButtonDefaults.buttonColors(Color.Blue)) {
            Text(text = "Easy", fontSize = 30.sp, color = Color.White)
        }
        Button(onClick = { onClick("medium", context)  }, colors = ButtonDefaults.buttonColors(Orange)) {
            Text(text = "Medium", fontSize = 30.sp, color = Color.White)
        }
        Button(onClick = { onClick("hard", context)  }, colors = ButtonDefaults.buttonColors(Color.Red)) {
            Text(text = "Hard", fontSize = 30.sp, color = Color.White)
        }
        Button(onClick = { onClick("random", context)  }, colors = ButtonDefaults.buttonColors(Color.Magenta)) {
            Text(text = "Random", fontSize = 30.sp, color = Color.White)
        }
    }
}

// 1 2 3
// 4 5 6
// 7 8 9

fun onClick(difficulty: String, context: Context) {
    Toast.makeText(context, difficulty, Toast.LENGTH_SHORT).show();
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SudokuTheme {
        SudokuApp()
    }
}