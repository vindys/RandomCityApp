package com.example.randomcityapp.view.common.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.core.graphics.toColorInt

fun String.toColorOrDefault(default: Color = Color.Gray): Color {
    val namedColors = mapOf(
        "Red" to Color.Red,
        "Green" to Color.Green,
        "Blue" to Color.Blue,
        "Yellow" to Color.Yellow,
        "Orange" to Color(0xFFFFA500),
        "Purple" to Color(0xFF800080),
        "Pink" to Color(0xFFFFC0CB),
        "Cyan" to Color.Cyan,
        "Gray" to Color.Gray,
        "White" to Color.White,
        "Black" to Color.Black
    )
    val normalized = this.trim().replaceFirstChar { it.uppercase() }
    return namedColors[normalized] ?: runCatching {
        Color(this.toColorInt())
    }.getOrElse { default }
}

fun Color.contrastingTextColor(): Color {
    // Convert to luminance and pick black or white
    return if (this.luminance() > 0.5f) Color.Black else Color.White
}

