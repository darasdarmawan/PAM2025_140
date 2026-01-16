package com.example.moodcare2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodcare2.data.repository.MoodCareRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditMoodViewModel(
    private val repository: MoodCareRepository
) : ViewModel() {

    private val _moodState = MutableStateFlow<MoodState>(MoodState.Idle)
    val moodState: StateFlow<MoodState> = _moodState

    fun getMoodById(moodId: Int) {
        viewModelScope.launch {
            _moodState.value = MoodState.Loading
            try {
                val response = repository.getMoodById(moodId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.success) {
                        _moodState.value =
                            MoodState.SingleMoodSuccess(body.data)
                    } else {
                        _moodState.value =
                            MoodState.Error("Mood tidak ditemukan")
                    }
                } else {
                    _moodState.value =
                        MoodState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _moodState.value =
                    MoodState.Error(e.message ?: "Error tidak diketahui")
            }
        }
    }

    fun updateMood(
        moodId: Int,
        moodLevel: String,
        description: String?,
        halBersyukur: String?,
        halSedih: String?,
        halPerbaikan: String?,
        tanggal: String
    ) {
        if (description.isNullOrBlank()) {
            _moodState.value = MoodState.Error("Deskripsi perasaan wajib diisi")
            return
        }
        viewModelScope.launch {
            _moodState.value = MoodState.Loading
            try {
                val response = repository.updateMood(
                    moodId, moodLevel, description, halBersyukur, halSedih, halPerbaikan, tanggal
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    _moodState.value = MoodState.OperationSuccess("Mood berhasil diperbarui!")
                } else {
                    _moodState.value = MoodState.Error("Gagal memperbarui mood")
                }
            } catch (e: Exception) {
                _moodState.value = MoodState.Error(e.message ?: "Error tidak diketahui")
            }
        }
    }

    fun resetState() {
        _moodState.value = MoodState.Idle
    }
}
