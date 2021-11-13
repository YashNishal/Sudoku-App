package com.example.sudoku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInteropFilter
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
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import maes.tech.intentanim.CustomIntent.customType


@DelicateCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        @Suppress("DEPRECATION")
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = resources.getColor(R.color.transparent)
        }
        super.onCreate(savedInstanceState)
        setContent {
            SudokuTheme {
                SudokuApp()
            }
        }
    }
}

@DelicateCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SudokuApp() {

    val loading = remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        delay(1000)
        visible = true
    }
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
        Background(loading)
        HeadingText()
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = visible,
            enter = fadeIn(animationSpec = tween(2000))
        ) {
            ButtonsAndProgressBar(loading)
        }
    }
}

// COMPONENTS
@Composable
fun Background(loading: MutableState<Boolean>) {
    val spacerHeight by animateFloatAsState(
        targetValue = if (loading.value) 1f else 0.7f,
        tween(1000)
    )
    val delayController = remember { mutableStateOf(true) }
    LaunchedEffect(true) {
        stateChange(delayController)
    }
    val fc1 by animateColorAsState(
        targetValue = if (delayController.value) DColor0 else DColor1, infiniteRepeatable(
            tween(5000)
        )
    )
    val fc2 by animateColorAsState(
        targetValue = if (delayController.value) DColor3 else DColor2, infiniteRepeatable(
            tween(5000)
        )
    )
    BoxWithConstraints(
        Modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(
                    fc1,
                    fc2
                )
            )
        )
    ) {
        val width = constraints.maxWidth * 1.25f
        val height = constraints.maxHeight / 2f

        // Medium colored path
        val mediumColoredPoint1 = Offset(0f, height * 0.3f)
        val mediumColoredPoint2 = Offset(width * 0.1f, height * 0.35f)
        val mediumColoredPoint3 = Offset(width * 0.4f, height * 0.05f)
        val mediumColoredPoint4 = Offset(width * 0.75f, height * 0.7f)
        val mediumColoredPoint5 = Offset(width * 1.4f, -height)

        val mediumColoredPath = Path().apply {
            moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
            standardQuadFromTo(mediumColoredPoint1, mediumColoredPoint2)
            standardQuadFromTo(mediumColoredPoint2, mediumColoredPoint3)
            standardQuadFromTo(mediumColoredPoint3, mediumColoredPoint4)
            standardQuadFromTo(mediumColoredPoint4, mediumColoredPoint5)
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
            Spacer(modifier = Modifier.fillMaxHeight(spacerHeight))
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
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
}


@Composable
fun HeadingText() {
    Text(
        text = "SUDOKU",
        fontSize = 70.sp,
        fontWeight = FontWeight.ExtraLight,
        color = TextWhite,
        letterSpacing = 10.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 70.dp)
    )
}

@DelicateCoroutinesApi
@ExperimentalComposeUiApi
@Composable
fun ButtonsAndProgressBar(loading: MutableState<Boolean>) {
    // for progressBar
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.fillMaxHeight(0.325f))
        Text(
            modifier = Modifier
                .padding(4.dp)
                .pointerInteropFilter {
                    if (it.action == ACTION_UP) {
                        onClick("easy", context, loading)
                    }
                    true
                },
            text = "EASY",
            fontSize = 24.sp,
            color = TextWhite,
            fontWeight = FontWeight.Light,
            letterSpacing = 5.sp
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "MEDIUM",
            fontSize = 24.sp,
            color = TextWhite,
            letterSpacing = 5.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(4.dp)
                .pointerInteropFilter {
                    if (it.action == ACTION_UP) {
                        onClick("medium", context, loading)
                    }
                    true
                }
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "HARD",
            fontSize = 24.sp,
            color = TextWhite,
            letterSpacing = 5.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(4.dp)
                .pointerInteropFilter {
                    if (it.action == ACTION_UP) {
                        onClick("hard", context, loading)
                    }
                    true
                }
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "RANDOM",
            fontSize = 24.sp,
            color = TextWhite,
            letterSpacing = 5.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(4.dp)
                .pointerInteropFilter {
                    if (it.action == ACTION_UP) {
                        onClick("random", context, loading)
                    }
                    true
                }
        )
        Spacer(modifier = Modifier.height(70.dp))
        if (loading.value)
            CircularProgressIndicator(color = Color.White)
    }
}


// OTHER HELPER FUNCTIONS
@ExperimentalComposeUiApi
@DelicateCoroutinesApi
fun onClick(difficulty: String, context: Context, loading: MutableState<Boolean>) {
    getData(difficulty, context, loading)
}

@ExperimentalComposeUiApi
@DelicateCoroutinesApi
fun getData(difficulty: String, context: Context, loading: MutableState<Boolean>) {
    val queue = Volley.newRequestQueue(context)
    val url = "https://sugoku.herokuapp.com/board?difficulty=${difficulty}"


    if (isConnected()) {
        loading.value = true
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Display the first 500 characters of the response string.
                Log.d("RESPONSE : $difficulty ", response.toString())
                initialiseMatrix(response.toString(), context, loading)
            },
            { Log.d("RESPONSE : ", "Error") })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    } else {
        Toasty
            .error(context, "No Internet available", Toast.LENGTH_LONG, true)
            .show()
    }
    // Request a string response from the provided URL.
}


@ExperimentalComposeUiApi
@DelicateCoroutinesApi
fun initialiseMatrix(response: String, context: Context, loading: MutableState<Boolean>) {
    var k = 11
    var row = 0
    var col = 0
    while (true) {
        val c: Char = response[k]
        if (c == ']')
            break
        if (c == ',') {
            row++
            col = 0
            k += 2
            continue
        }
        matrix[row][col] = c - '0'
        original[row][col] = c - '0'
        col++
        k += 2
    }
    GlobalScope.launch {
        delay(1200)
        loading.value = false
    }
    startActivity(context, Intent(context, PrimaryActivity::class.java), null)
    customType(context, "fadein-to-fadeout")
}


@DelicateCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SudokuTheme {
        SudokuApp()
    }
}