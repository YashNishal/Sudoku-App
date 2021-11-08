package com.example.sudoku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sudoku.ui.theme.*


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
    val delayController = remember{ mutableStateOf(true)}
    LaunchedEffect(true) {
        stateChange(delayController)
    }
    val fc1 by animateColorAsState(targetValue = if (delayController.value) DColor0 else DColor1, infiniteRepeatable(
        tween(5000)))
    val fc2 by animateColorAsState(targetValue = if (delayController.value) DColor3 else DColor2,  infiniteRepeatable(
        tween(5000)))
    val fc = if (isSystemInDarkTheme()) listOf(Color1, Color2) else listOf(DColor1, DColor2)
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
        BoxWithConstraints(Modifier.background(brush = Brush.linearGradient(colors = listOf(fc1, fc2)))) {
            val width = constraints.maxWidth*1.25f
            val height = constraints.maxHeight/2f

            // Medium colored path
            val mediumColoredPoint1 = Offset(0f,height * 0.3f)
            val mediumColoredPoint2 = Offset(width * 0.1f,height * 0.35f)
            val mediumColoredPoint3 = Offset(width* 0.4f,height * 0.05f)
            val mediumColoredPoint4 = Offset(width * 0.75f,height * 0.7f)
            val mediumColoredPoint5 = Offset(width * 1.4f,-height )

            val mediumColoredPath = Path().apply {
                moveTo(mediumColoredPoint1.x,mediumColoredPoint1.y)
                standardQuadFromTo(mediumColoredPoint1,mediumColoredPoint2)
                standardQuadFromTo(mediumColoredPoint2,mediumColoredPoint3)
                standardQuadFromTo(mediumColoredPoint3,mediumColoredPoint4)
                standardQuadFromTo(mediumColoredPoint4,mediumColoredPoint5)
                lineTo(width + 100f, height + 300f)
                lineTo(-100f, height + 100f)
                close()
            }


            // Light colored path
            val lightPoint1 = Offset(0f, height * 0.35f)
            val lightPoint2 = Offset(width * 0.1f, height * 0.4f)
            val lightPoint3 = Offset(width * 0.3f, height * 0.35f)
            val lightPoint4 = Offset(width * 0.65f, height)
            val lightPoint5 = Offset(width * 1.4f, -height / 3f)

            val lightColoredPath = Path().apply {
                moveTo(lightPoint1.x, lightPoint1.y)
                standardQuadFromTo(lightPoint1, lightPoint2)
                standardQuadFromTo(lightPoint2, lightPoint3)
                standardQuadFromTo(lightPoint3, lightPoint4)
                standardQuadFromTo(lightPoint4, lightPoint5)
                lineTo(width + 100f, height + 100f)
                lineTo(-100f, height + 100f)
                close()
            }
            Column {
                Spacer(modifier = Modifier.fillMaxHeight(0.65f))
                Canvas(
                    modifier = Modifier.fillMaxSize()) {
                    drawPath(
                        path = mediumColoredPath,
                        color = Curve,
                    )
                    drawPath(
                        path = lightColoredPath,
                        color = Curve
                    )
                }
            }
        }
        Text(text = "SUDOKU", fontSize = 50.sp, fontWeight = FontWeight.Light, color = TextWhite, letterSpacing = 1.5.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 40.dp))
        Buttons()
    }
}

@Composable
fun Buttons() {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = { onClick("easy", context) }, colors = ButtonDefaults.buttonColors(Easy)) {
            Text(text = "Easy", fontSize = 30.sp, color = TextWhite, fontWeight = FontWeight.Bold)
        }
        Button(onClick = { onClick("medium", context)  }, colors = ButtonDefaults.buttonColors(Orange)) {
            Text(text = "Medium", fontSize = 30.sp, color = TextWhite)
        }
        Button(onClick = { onClick("hard", context)  }, colors = ButtonDefaults.buttonColors(Color.Red)) {
            Text(text = "Hard", fontSize = 30.sp, color = TextWhite)
        }
        Button(onClick = { onClick("random", context)  }, colors = ButtonDefaults.buttonColors(Color.Magenta)) {
            Text(text = "Random", fontSize = 30.sp, color = TextWhite)
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