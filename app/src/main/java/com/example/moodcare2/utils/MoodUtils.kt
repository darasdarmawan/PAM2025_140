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

