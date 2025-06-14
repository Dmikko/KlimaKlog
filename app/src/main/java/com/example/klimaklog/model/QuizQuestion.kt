package com.example.klimaklog.model

import kotlinx.serialization.Serializable

// Repræsenterer ét multiple-choice spørgsmål i quizzen.
// @serializable kan hurtigt gemmes/hentes som JSON

@Serializable
data class QuizQuestion(
    val text: String,
    val options: List<String>,
    val correctAnswer: String,
    val level: String
)
