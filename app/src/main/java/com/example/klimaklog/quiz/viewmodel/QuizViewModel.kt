package com.example.klimaklog.quiz.viewmodel

// HC

import android.annotation.SuppressLint
import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.klimaklog.dataStore
import com.example.klimaklog.quiz.model.QuizQuestion
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class QuizQuestionWrapper(val questions: List<QuizQuestion>)

private val Application.dataStore by preferencesDataStore(name = "klima_prefs")
private val TOTAL_POINTS_KEY = intPreferencesKey("total_klima_points")
private val PERSONAL_POINTS_KEY = intPreferencesKey("personal_points")

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    companion object {
        private val json = Json { ignoreUnknownKeys = true }
    }

    private val _totalPoints = MutableStateFlow(0)
    val totalPoints = _totalPoints.asStateFlow()

    private val _personalPoints = MutableStateFlow(0)
    val personalPoints = _personalPoints.asStateFlow()

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
                val total = prefs[TOTAL_POINTS_KEY] ?: 0
                val personal = prefs[PERSONAL_POINTS_KEY] ?: 0
                total to personal
            }.collect { (total, personal) ->
                _totalPoints.value = total
                _personalPoints.value = personal
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

    fun loadCustomQuestions(questions: List<QuizQuestion>) {
        _allQuestions.clear()
        _allQuestions.addAll(questions)
        currentIndex = 0
        _currentQuestion.value = _allQuestions.getOrNull(currentIndex)
        _userAnswer.value = null
        _points.value = 0
    }

    fun submitAnswer(answer: String) {
        // Undgå dobbeltklik
        if (_userAnswer.value != null) return

        _userAnswer.value = answer

        if (checkAnswerIsCorrect()) {
            viewModelScope.launch {
                _totalPoints.value += 10
                _points.value += 10
                if (_currentQuestion.value?.level == "personal") {
                    _personalPoints.value += 10
                    context.dataStore.edit { it[PERSONAL_POINTS_KEY] = _personalPoints.value }
                }
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
        return if (currentIndex < _allQuestions.size - 1) {
            currentIndex++
            _currentQuestion.value = _allQuestions[currentIndex]
            _userAnswer.value = null
            true
        } else {
            // NYT: quiz er færdig, ryd spørgsmålet
            _currentQuestion.value = null
            false
        }
    }

    fun resetAllPoints() {
        viewModelScope.launch {
            _totalPoints.value = 0
            _personalPoints.value = 0
            context.dataStore.edit {
                it[TOTAL_POINTS_KEY] = 0
                it[PERSONAL_POINTS_KEY] = 0
            }
        }
    }




    fun resetQuiz() {
        currentIndex = 0
        _currentQuestion.value = _allQuestions.getOrNull(currentIndex)
        _userAnswer.value = null
        _points.value = 0
    }
}
