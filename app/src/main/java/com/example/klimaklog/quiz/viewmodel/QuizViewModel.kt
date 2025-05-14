package com.example.klimaklog.viewmodel

import androidx.lifecycle.ViewModel
import com.example.klimaklog.quiz.model.QuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizViewModel : ViewModel() {

    // Spørgsmål til "Let" niveau
    private val easyQuestions = listOf(
        QuizQuestion(
            question = "Hvor meget CO₂ udleder det cirka at producere en bomulds-t-shirt?",
            options = listOf("0,3 kg", "2–4 kg", "9 kg", "15 kg"),
            correctAnswer = "2–4 kg"
        ),
        QuizQuestion(
            question = "Hvilken fødevare har typisk det største CO₂-aftryk?",
            options = listOf("Kartofler", "Oksekød", "Æbler", "Kylling"),
            correctAnswer = "Oksekød"
        )
        // Tilføj flere spørgsmål her
    )

    private var currentIndex = 0
    val currentQuestion: QuizQuestion
        get() = easyQuestions[currentIndex]

    private val _userAnswer = MutableStateFlow<String?>(null)
    val userAnswer = _userAnswer.asStateFlow()

    fun submitAnswer(answer: String) {
        _userAnswer.value = answer
    }

    fun checkAnswerIsCorrect(): Boolean {
        return _userAnswer.value == currentQuestion.correctAnswer
    }

    fun nextQuestion(): Boolean {
        if (currentIndex < easyQuestions.size - 1) {
            currentIndex++
            _userAnswer.value = null
            return true
        }
        return false
    }

    fun resetQuiz() {
        currentIndex = 0
        _userAnswer.value = null
    }
}
