package com.example.klimaklog.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.klimaklog.viewmodel.QuizViewModel
import com.example.klimaklog.ui.theme.klimaFont
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel


import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color





@Composable
fun MellemQuizScreen(navController: NavController, viewModel: QuizViewModel = viewModel()) {
    val question by viewModel.currentQuestion.collectAsState()
    val userAnswer by viewModel.userAnswer.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadQuestions("mellem")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Let Quiz", fontSize = 28.sp, fontFamily = klimaFont)

        Spacer(modifier = Modifier.height(24.dp))

        if (question == null) {
            Text("Indlæser spørgsmål...", fontFamily = klimaFont)
            return
        }

        Text(text = question!!.text, fontSize = 20.sp, fontFamily = klimaFont)

        Spacer(modifier = Modifier.height(16.dp))

        question!!.options.forEach { option ->
            Button(
                onClick = { viewModel.submitAnswer(option) },
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            ) {
                Text(option)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        userAnswer?.let {
            val correct = viewModel.checkAnswerIsCorrect()
            Text(
                text = if (correct) "Korrekt!" else "Forkert",
                color = if (correct) Color.Green else Color.Red,
                fontSize = 18.sp,
                fontFamily = klimaFont
            )
        }
    }
}
