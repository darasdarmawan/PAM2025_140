package com.example.moodcare2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodcare2.data.model.Mood
import com.example.moodcare2.data.repository.MoodCareRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class GraphState {
    object Idle : GraphState()
    object Loading : GraphState()
    data class Success(val moods: List<Mood>) : GraphState()
    data class Error(val message: String) : GraphState()
}

class GraphViewModel(
    private val repository: MoodCareRepository
) : ViewModel() {

    private val _graphState = MutableStateFlow<GraphState>(GraphState.Idle)
    val graphState: StateFlow<GraphState> = _graphState

    fun searchMoodByRange(startDate: String, endDate: String) {
        viewModelScope.launch {
            _graphState.value = GraphState.Loading
            try {
                val response = repository.searchMoodByRange(startDate, endDate)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.success) {
                        _graphState.value = GraphState.Success(body.data)
                    } else {
                        _graphState.value = GraphState.Error("Tidak ada data pada rentang tanggal tersebut")
                    }
                } else {
                    _graphState.value = GraphState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _graphState.value = GraphState.Error(e.message ?: "Error tidak diketahui")
            }
        }
    }

    fun resetState() {
        _graphState.value = GraphState.Idle
    }
}
