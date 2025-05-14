package com.example.klimaklog.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.klimaklog.ui.theme.klimaFont
import com.example.klimaklog.viewmodel.QuizViewModel


@Composable
fun LetQuizScreen(navController: NavController, viewModel: QuizViewModel = viewModel()) {
    val question = viewModel.currentQuestion
    val userAnswer by viewModel.userAnswer.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val klimaFont = null
        Text(
            text = "Klima Klog\nQuiz",
            style = TextStyle(fontFamily = klimaFont, fontSize = 40.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Let",
            style = TextStyle(fontFamily = klimaFont, fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .background(Color(0xFFDFFFD9), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Text(
                text = question.text,
                fontFamily = klimaFont,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        question.options.forEach { option ->
            Button(
                onClick = { viewModel.submitAnswer(option) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBFFFB8)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.8f)
            ) {
                Text(option, fontFamily = klimaFont)
            }
        }

        if (userAnswer != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (userAnswer == question.correctAnswer) "Korrekt! +10" else "Forkert 😢",
                style = TextStyle(fontFamily = klimaFont, fontSize = 20.sp),
                color = if (userAnswer == question.correctAnswer) Color.Green else Color.Red
            )
        }
    }
}
