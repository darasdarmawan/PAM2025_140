package com.example.moodcare2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodcare2.data.model.User
import com.example.moodcare2.data.repository.MoodCareRepository
import com.example.moodcare2.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    object UpdateSuccess : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel(
    private val repository: MoodCareRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState

    fun updateProfile(nama: String, imageFile: File?) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val response =
                    repository.updateProfileWithImage(nama, imageFile)

                if (response.isSuccessful && response.body()?.success == true) {
                    val user = response.body()!!.data

                    sessionManager.updateUserData(
                        user.nama,
                        user.fotoProfil
                    )

                    _profileState.value = ProfileState.UpdateSuccess
                } else {
                    _profileState.value =
                        ProfileState.Error("Gagal memperbarui profil")
                }
            } catch (e: Exception) {
                _profileState.value =
                    ProfileState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun resetState() {
        _profileState.value = ProfileState.Idle
    }
}
