package com.example.wanderer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.wanderer.R


//you need to define font families BEFORE you get to work them

// Define Reglo Font Family
val RegloFamily = FontFamily(
    Font(R.font.reglo_bold, FontWeight.Bold)
)

// Define Roboto Font Family
val RobotoFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    // Titles use Reglo
    headlineLarge = TextStyle(
        fontFamily = RegloFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = 1.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RegloFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    // Body text uses Roboto
    bodyLarge = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)
