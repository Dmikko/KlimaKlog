package com.example.klimaklog.quiz.model

import kotlinx.serialization.Serializable


@Serializable
data class QuizQuestion(
    val text: String,
    val options: List<String>,
    val correctAnswer: String,
    val level: String
)
