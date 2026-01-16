package com.example.moodcare2

import android.app.Application
import com.example.moodcare2.data.repository.MoodCareRepository
import com.example.moodcare2.utils.SessionManager

class MoodCareApplication : Application() {

    val sessionManager: SessionManager by lazy {
        SessionManager(this)
    }

    val repository: MoodCareRepository by lazy {
        MoodCareRepository(sessionManager)
    }
}