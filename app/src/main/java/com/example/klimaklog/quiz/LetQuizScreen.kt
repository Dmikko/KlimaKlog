package com.example.klimaklog.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.klimaklog.ui.theme.klimaFont
import com.example.klimaklog.viewmodel.QuizViewModel

@Composable
fun LetQuizScreen(navController: NavController, viewModel: QuizViewModel = viewModel()) {
    val question by viewModel.currentQuestion.collectAsState()
    val userAnswer by viewModel.userAnswer.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadQuestions("let")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔙 Tilbageknap
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Tilbage"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🧠 Titel
        Text(
            text = "Let Quiz",
            fontFamily = klimaFont,
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ⏳ Indlæser
        if (question == null) {
            CircularProgressIndicator()
            return
        }

        // ❓ Spørgsmål
        Box(
            modifier = Modifier
                .background(Color(0xFFDFFFD9), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Text(
                text = question!!.text,
                fontFamily = klimaFont,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 🔘 Svarmuligheder
        question!!.options.forEach { option ->
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

        // ✅ Resultat
        if (userAnswer != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (userAnswer == question!!.correctAnswer) "Korrekt! +10" else "Forkert 😢",
                fontFamily = klimaFont,
                fontSize = 20.sp,
                color = if (userAnswer == question!!.correctAnswer) Color.Green else Color.Red
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.nextQuestion() }) {
                Text("Næste", fontFamily = klimaFont)
            }
        }
    }
}
