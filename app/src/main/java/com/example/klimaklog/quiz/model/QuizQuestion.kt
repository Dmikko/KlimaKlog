package com.example.klimaklog.quiz.model

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)
