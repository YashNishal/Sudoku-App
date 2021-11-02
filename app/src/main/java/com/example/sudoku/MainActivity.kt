package com.example.sudoku

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Cyan) {
        Buttons()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun Buttons() {
    var context = LocalContext.current;
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
        Button(onClick = { onClick("easy", context) }) {
            Text(text = "Easy")
        }
        Button(onClick = { onClick("medium", context)  }) {
            Text(text = "Medium")
        }
        Button(onClick = { onClick("hard", context)  }) {
            Text(text = "Hard")
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