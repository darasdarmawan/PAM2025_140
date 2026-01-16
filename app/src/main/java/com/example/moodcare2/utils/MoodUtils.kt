package com.example.moodcare2.utils

import androidx.compose.ui.graphics.Color

fun getMoodEmoji(moodLevel: String): String {
    return when (moodLevel.lowercase()) {
        "sangat baik" -> "😄"
        "baik" -> "😊"
        "netral" -> "😐"
        "buruk" -> "😔"
        "sangat buruk" -> "😢"
        else -> "😐"
    }
}

fun getMoodLabel(moodLevel: String): String {
    return when (moodLevel.lowercase()) {
        "sangat baik" -> "Sangat Baik"
        "baik" -> "Baik"
        "netral" -> "Netral"
        "buruk" -> "Buruk"
        "sangat buruk" -> "Sangat Buruk"
        else -> "Tidak Diketahui"
    }
}

fun getMoodColor(moodLevel: String): Color {
    return when (moodLevel.lowercase()) {
        "sangat baik" -> Color(0xFFE91E63)
        "baik" -> Color(0xFF8BC34A)
        "netral" -> Color(0xFF00BCD4)
        "buruk" -> Color(0xFFFF7700)
        "sangat buruk" -> Color(0xFFF44336)
        else -> Color(0xFFCDDC39)
    }
}