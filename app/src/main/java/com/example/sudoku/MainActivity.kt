package com.example.sudoku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sudoku.ui.theme.Orange
import com.example.sudoku.ui.theme.SudokuTheme



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
fun Buttons() {
    val context = LocalContext.current
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



fun onClick(difficulty: String, context: Context) {
    change = "1"
    getData(difficulty,context)
}

fun getData(difficulty: String,context: Context) {
    val queue = Volley.newRequestQueue(context)
    val url = "https://sugoku.herokuapp.com/board?difficulty=${difficulty}"

    // Request a string response from the provided URL.
    val stringRequest = StringRequest(
        Request.Method.GET, url,
        { response ->
            // Display the first 500 characters of the response string.
            Log.d("RESPONSE : $difficulty ",response.toString() )
            initialiseMatrix(response.toString(),context)
        },
        { Log.d("RESPONSE : ","Error") })

    // Add the request to the RequestQueue.
    queue.add(stringRequest)
}

fun initialiseMatrix(response: String,context: Context) {
    Log.d("string : ",response)

    var k  = 11
    var row  = 0
    var col = 0
    while(true) {
        val c : Char = response[k]
        if(c == ']')
            break
        if(c == ','){
            row++
            col = 0
            k += 2
            continue
        }
        matrix[row][col] = c-'0'
        original[row][col] = c-'0'
        col++
        k += 2
    }

    for( i in 0..8) {
        for(j in 0..8) {
            Log.d("  i = $i  j = $j : value = ","${matrix[i][j]}")
        }
    }

    startActivity(context,Intent(context,PrimaryActivity::class.java),null)
    Log.e("matrix :" , " initialised")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SudokuTheme {
        SudokuApp()
    }
}