package com.example.moodcare2.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.moodcare2.data.model.User

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("MoodCarePrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHOTO = "foto_profil"

    }

    fun saveToken(token: String) {
        val tokenToSave = if (token.startsWith("Bearer ")) {
            token
        } else {
            "Bearer $token"
        }
        prefs.edit().putString(KEY_TOKEN, tokenToSave).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUserData(userId: Int, name: String, email: String, foto_profil: String? = null) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_PHOTO, foto_profil)
            apply()
        }
    }
    fun updateUserData(nama: String, foto_profil: String?) {
        prefs.edit().apply {
            putString(KEY_USER_NAME, nama)
            putString(KEY_USER_PHOTO, foto_profil)
            apply()
        }
    }
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    fun getNama(): String? {
        return getUserName()
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    fun getEmail(): String? {
        return getUserEmail()
    }
    fun getFotoProfile(): String? {
        return prefs.getString(KEY_USER_PHOTO, null)
    }

    fun saveLoginSession(token: String, user: User) {
        saveToken(token)
        saveUserData(user.user_id, user.nama, user.email, user.fotoProfil)
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}