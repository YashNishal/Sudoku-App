package com.example.sudoku.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.example.sudoku.R
import kotlin.coroutines.coroutineContext


val fonts = FontFamily (
    Font(R.font.source_sans_pro, weight = FontWeight.Normal),
    Font(R.font.source_sans_pro_bold, weight = FontWeight.Bold),
    Font(R.font.source_sans_pro_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.source_sans_pro_light, weight = FontWeight.Light),
    Font(R.font.source_sans_pro_black, weight = FontWeight.Black)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)