package com.example.klimaklog.quiz.viewmodel

import androidx.lifecycle.ViewModel
import com.example.klimaklog.quiz.model.QuizQuestion

class QuizViewModel : ViewModel() {
    private val allEasyQuestions = listOf(
        QuizQuestion(
            question = "Hvor meget CO₂ udleder det cirka at producere en bomulds-t-shirt?",
            options = listOf("0,3 kg", "2–4 kg", "9 kg", "15 kg"),
            correctAnswer = "2–4 kg"
        ),
        QuizQuestion(
            question = "Hvilken fødevare har typisk højest CO₂-aftryk?",
            options = listOf("Kartofler", "Oksekød", "Æbler", "Kylling"),
            correctAnswer = "Oksekød"
        )
        // Tilføj flere spørgsmål her
    )

    private var currentIndex = 0
    val currentQuestion: QuizQuestion
        get() = allEasyQuestions[currentIndex]

    fun checkAnswer(selected: String): Boolean {
        return selected == currentQuestion.correctAnswer
    }

    fun nextQuestion(): Boolean {
        return if (currentIndex < allEasyQuestions.size - 1) {
            currentIndex++
            true
        } else {
            false
        }
    }

    fun resetQuiz() {
        currentIndex = 0
    }
}
