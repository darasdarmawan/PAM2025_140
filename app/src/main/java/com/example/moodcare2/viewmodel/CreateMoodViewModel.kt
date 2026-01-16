package com.example.moodcare2.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodcare2.data.model.Mood
import com.example.moodcare2.data.repository.MoodCareRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MoodState {
    object Idle : MoodState()
    object Loading : MoodState()
    data class Success(val moods: List<Mood>) : MoodState()
    data class SingleMoodSuccess(val mood: Mood) : MoodState()
    data class OperationSuccess(val message: String) : MoodState()
    data class Error(val message: String) : MoodState()
}

class CreateMoodViewModel(
    private val repository: MoodCareRepository
) : ViewModel() {

    private val _moodState = MutableStateFlow<MoodState>(MoodState.Idle)
    val moodState: StateFlow<MoodState> = _moodState

    fun getAllMoods() {
        viewModelScope.launch {
            _moodState.value = MoodState.Loading
            try {
                val response = repository.getAllMoods()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.success) {
                        _moodState.value = MoodState.Success(body.data)
                    } else {
                        _moodState.value = MoodState.Error("Gagal memuat data")
                    }
                } else {
                    _moodState.value = MoodState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _moodState.value = MoodState.Error(e.message ?: "Error tidak diketahui")
            }
        }
    }

    fun searchMoodByDate(date: String) {
        viewModelScope.launch {
            _moodState.value = MoodState.Loading
            try {
                val response = repository.searchMoodByDate(date)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.success) {
                        _moodState.value = MoodState.Success(body.data)
                    } else {
                        _moodState.value = MoodState.Error("Tidak ada data pada tanggal tersebut")
                    }
                } else {
                    _moodState.value = MoodState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _moodState.value = MoodState.Error(e.message ?: "Error tidak diketahui")
            }
        }
    }

    fun createMood(
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
                val token = repository.sessionManager.getToken()
                println("🔑 DEBUG Token: ${token?.take(20)}...")

                if (token.isNullOrBlank()) {
                    _moodState.value = MoodState.Error("Sesi berakhir. Silakan login ulang.")
                    return@launch
                }

                println("📤 DEBUG Sending: mood=$moodLevel, date=$tanggal")

                val response = repository.createMood(
                    moodLevel, description, halBersyukur, halSedih, halPerbaikan, tanggal
                )

                println("📥 DEBUG Response Code: ${response.code()}")
                println("📥 DEBUG Response Success: ${response.isSuccessful}")
                println("📥 DEBUG Response Body: ${response.body()}")

                if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string()
                    println("❌ DEBUG Error Body: $errorBody")
                    _moodState.value = MoodState.Error("Error ${response.code()}: $errorBody")
                    return@launch
                }

                if (response.isSuccessful && response.body()?.success == true) {
                    _moodState.value = MoodState.OperationSuccess("Mood berhasil ditambahkan!")
                } else {
                    val message = response.body()?.message ?: "Gagal menambahkan mood"
                    _moodState.value = MoodState.Error(message)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                println("💥 DEBUG Exception: ${e.javaClass.simpleName} - ${e.message}")
                _moodState.value = MoodState.Error(
                    when (e) {
                        is java.net.SocketTimeoutException -> "Koneksi timeout. Cek jaringan Anda."
                        is java.net.UnknownHostException -> "Tidak dapat terhubung ke server."
                        is java.io.EOFException -> "Server tidak merespons dengan benar (EOF)"
                        else -> "Error: ${e.message}"
                    }
                )
            }
        }
    }
    fun deleteMood(moodId: Int) {
        viewModelScope.launch {
            _moodState.value = MoodState.Loading
            try {
                val response = repository.deleteMood(moodId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _moodState.value = MoodState.OperationSuccess("Mood berhasil dihapus!")
                } else {
                    _moodState.value = MoodState.Error("Gagal menghapus mood")
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
