package com.example.moodcare2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodcare2.data.model.User
import com.example.moodcare2.data.model.ErrorResponse
import com.example.moodcare2.data.repository.MoodCareRepository
import com.example.moodcare2.utils.SessionManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val repository: MoodCareRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val response = repository.login(email, password)

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null && body.success) {
                        sessionManager.saveLoginSession(body.token, body.user)
                        _authState.value = AuthState.Success(body.user)
                    } else {
                        _authState.value =
                            AuthState.Error(body?.message ?: "Login gagal")
                    }

                } else {
                    _authState.value = AuthState.Error(parseError(response.errorBody()?.string()))
                }

            } catch (e: Exception) {
                _authState.value =
                    AuthState.Error("Tidak dapat terhubung ke server")
            }
        }
    }

    fun register(nama: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val response = repository.register(nama, email, password)

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null && body.success) {
                        sessionManager.saveLoginSession(body.token, body.user)
                        _authState.value = AuthState.Success(body.user)
                    } else {
                        _authState.value =
                            AuthState.Error(body?.message ?: "Register gagal")
                    }

                } else {
                    _authState.value = AuthState.Error(parseError(response.errorBody()?.string()))
                }

            } catch (e: Exception) {
                _authState.value =
                    AuthState.Error("Tidak dapat terhubung ke server")
            }
        }
    }

    private fun parseError(errorBody: String?): String {
        return try {
            val errorResponse =
                Gson().fromJson(errorBody, ErrorResponse::class.java)
            errorResponse.message
        } catch (e: Exception) {
            "Email atau password salah"
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
