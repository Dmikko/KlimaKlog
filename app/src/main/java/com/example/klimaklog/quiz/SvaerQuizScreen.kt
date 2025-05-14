package com.example.klimaklog.quiz

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import com.example.klimaklog.R
import com.example.klimaklog.viewmodel.QuizViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SvaerQuizScreen(navController: NavController, viewModel: QuizViewModel = viewModel()) {
    val klimaFont = remember {
        try {
            FontFamily(Font(R.font.jolly_lodger))
        } catch (e: Exception) {
            FontFamily.Default
        }
    }

    val question by viewModel.currentQuestion.collectAsState()
    val userAnswer by viewModel.userAnswer.collectAsState()
    val points by viewModel.points.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadQuestions("svaer")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Let Quiz", fontFamily = klimaFont, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tilbage")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (question != null) {
                QuizQuestionUI(
                    question = question!!,
                    userAnswer = userAnswer,
                    points = points,
                    onAnswerSelected = { answer ->
                        viewModel.submitAnswer(answer)
                        viewModel.incrementPointsIfCorrect()
                    },
                    onNext = { viewModel.nextQuestion() },
                    font = klimaFont
                )

            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Indlæser spørgsmål...", fontFamily = klimaFont)
                }
            }
        }
    }
}
