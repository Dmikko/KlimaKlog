package com.example.klimaklog.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.klimaklog.data.dataStore
import com.example.klimaklog.quiz.model.QuizQuestion
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class QuizQuestionWrapper(val questions: List<QuizQuestion>)

private val Application.dataStore by preferencesDataStore(name = "klima_prefs")
private val TOTAL_POINTS_KEY = intPreferencesKey("total_klima_points")

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    companion object {
        private val json = Json { ignoreUnknownKeys = true }
    }

    private val _totalPoints = MutableStateFlow(0)
    val totalPoints = _totalPoints.asStateFlow()

    private val _allQuestions = mutableListOf<QuizQuestion>()
    private var currentIndex = 0

    private val _currentQuestion = MutableStateFlow<QuizQuestion?>(null)
    val currentQuestion = _currentQuestion.asStateFlow()

    private val _userAnswer = MutableStateFlow<String?>(null)
    val userAnswer = _userAnswer.asStateFlow()

    private val _points = MutableStateFlow(0)
    val points = _points.asStateFlow()

    init {
        loadSavedPoints()
    }

    private fun loadSavedPoints() {
        viewModelScope.launch {
            context.dataStore.data.map { prefs ->
                prefs[TOTAL_POINTS_KEY] ?: 0
            }.collect { savedPoints ->
                _totalPoints.value = savedPoints
            }
        }
    }

    private suspend fun saveTotalPoints(value: Int) {
        context.dataStore.edit { prefs ->
            prefs[TOTAL_POINTS_KEY] = value
        }
    }

    fun loadQuestions(difficulty: String) {
        viewModelScope.launch {
            try {
                val jsonString = context.assets.open("quiz/quiz_questions.json")
                    .bufferedReader().use { it.readText() }

                val parsed = json.decodeFromString<QuizQuestionWrapper>(jsonString)

                _allQuestions.clear()
                _allQuestions.addAll(parsed.questions.filter {
                    it.level.equals(difficulty, ignoreCase = true)
                })

                currentIndex = 0
                _currentQuestion.value = _allQuestions.getOrNull(currentIndex)
                _userAnswer.value = null
                _points.value = 0

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun submitAnswer(answer: String) {
        _userAnswer.value = answer
        if (checkAnswerIsCorrect()) {
            viewModelScope.launch {
                _totalPoints.value += 10
                saveTotalPoints(_totalPoints.value)
            }
        }
    }

    fun incrementPointsIfCorrect() {
        if (_userAnswer.value == _currentQuestion.value?.correctAnswer) {
            _points.value += 10
        }
    }

    fun checkAnswerIsCorrect(): Boolean {
        return _userAnswer.value == _currentQuestion.value?.correctAnswer
    }

    fun nextQuestion(): Boolean {
        if (currentIndex < _allQuestions.size - 1) {
            currentIndex++
            _currentQuestion.value = _allQuestions[currentIndex]
            _userAnswer.value = null
            return true
        }
        return false
    }

    fun resetQuiz() {
        currentIndex = 0
        _currentQuestion.value = _allQuestions.getOrNull(currentIndex)
        _userAnswer.value = null
        _points.value = 0
    }
}
