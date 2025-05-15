package com.example.klimaklog.quiz.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klimaklog.ai.getClimateInfoFromQuery
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    var result by mutableStateOf<String?>(null)
        private set

    fun searchQuery(query: String) {
        viewModelScope.launch {
            result = try {
                getClimateInfoFromQuery(query)
            } catch (e: Exception) {
                "Noget gik galt. Prøv igen senere."
            }
        }
    }
}
