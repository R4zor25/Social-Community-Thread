package hu.bme.aut.android.socialcommunitythread.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

//Light colors
val PrimaryLight = Color(0xFFFFFFFF)
val SecondaryLight = Color(0xFFF5F5DC)

@Composable
fun defaultTextColor() = if (isSystemInDarkTheme()) {
    Color.White
} else {
    Color(0xFF222222)
}

@Composable
fun secondaryTextColor() = if (isSystemInDarkTheme()) {
    Color(0xFFCCCCCC)
} else {
    Color.Black
}

@Composable
fun defaultIconColor() = if (isSystemInDarkTheme()) {
    Color.White
} else {
    Color.Black
}

@Composable
fun disabledIconColor() = if (isSystemInDarkTheme()) {
    Color.LightGray
} else {
    Color.DarkGray
}


//Dark colors
val PrimaryDark = Color(0xFF121212)
val SecondaryDark = Color(0xFF333333)

val darkColorPalette = darkColors(
    primary = PrimaryDark,
    primaryVariant = Color.White,
    secondary = SecondaryDark,
    secondaryVariant = Color.White,
    background = SecondaryDark,
    surface = Color.White,
    error = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.White,
)

val lightColorPalette = lightColors(
    primary = PrimaryLight,
    primaryVariant = Color.White,
    secondary = SecondaryLight,
    secondaryVariant = Color.White,
    background = SecondaryLight,
    surface = Color.White,
    error = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.White
)

@Composable
fun myAppTextFieldColors() =
    if (isSystemInDarkTheme()) {
        TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            disabledTextColor = Color.LightGray,
            cursorColor = Color.White,
            errorCursorColor = Color.Red,
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            leadingIconColor = Color.White,
            placeholderColor = Color.White,
            disabledPlaceholderColor = Color.White
        )
    } else {
        TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            disabledTextColor = Color.LightGray,
            cursorColor = Color.Black,
            errorCursorColor = Color.Red,
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            leadingIconColor = Color.Black,
            placeholderColor = Color.Black,
            disabledPlaceholderColor = Color.Black
        )
    }


@Composable
fun inputFieldBackgroundBrush() = Brush.horizontalGradient(colors = listOf(MaterialTheme.colors.secondary, MaterialTheme.colors.secondary))