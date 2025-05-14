package com.example.klimaklog.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.klimaklog.quiz.model.QuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class QuizQuestionWrapper(
    val questions: List<QuizQuestion>
)

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }
    }

    private val _allQuestions = mutableListOf<QuizQuestion>()
    private var currentIndex = 0

    private val _currentQuestion = MutableStateFlow<QuizQuestion?>(null)
    val currentQuestion = _currentQuestion.asStateFlow()

    private val _userAnswer = MutableStateFlow<String?>(null)
    val userAnswer = _userAnswer.asStateFlow()

    fun loadQuestions(difficulty: String) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val jsonString = context.assets.open("quiz/quiz_questions.json")
                    .bufferedReader()
                    .use { it.readText() }

                val parsed = json.decodeFromString<QuizQuestionWrapper>(jsonString)

                _allQuestions.clear()
                _allQuestions.addAll(parsed.questions.filter { it.level.equals(difficulty, ignoreCase = true) })
                currentIndex = 0
                _currentQuestion.value = _allQuestions.getOrNull(currentIndex)
                _userAnswer.value = null
            } catch (e: Exception) {
                e.printStackTrace() // Log til fejlsøgning
            }
        }
    }

    fun submitAnswer(answer: String) {
        _userAnswer.value = answer
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
    }
}
