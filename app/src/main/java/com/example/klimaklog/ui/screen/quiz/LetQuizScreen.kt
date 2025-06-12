package com.example.klimaklog.ui.screen.quiz

// HC og Mike

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.klimaklog.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.klimaklog.viewmodel.QuizViewModel
import com.example.klimaklog.ui.components.QuizQuestionUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LetQuizScreen(navController: NavController, viewModel: QuizViewModel = viewModel()) {
    val klimaFont = remember {
        try { FontFamily(Font(R.font.roboto)) } catch (e: Exception) { FontFamily.Default }
    }

    val question by viewModel.currentQuestion.collectAsState()
    val userAnswer by viewModel.userAnswer.collectAsState()
    val points by viewModel.points.collectAsState()

    var isQuizFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadQuestions("let")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Let Quiz", fontFamily = klimaFont, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tilbage")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                question != null -> {
                    QuizQuestionUI(
                        question = question!!,
                        userAnswer = userAnswer,
                        points = points,
                        onAnswerSelected = { answer ->
                            viewModel.submitAnswer(answer)
                        },
                        onNext = {
                            val hasNext = viewModel.nextQuestion()
                            if (!hasNext) isQuizFinished = true
                        },
                        font = klimaFont
                    )
                }

                isQuizFinished -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Du har gennemført quizzen!", fontFamily = klimaFont, fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Point: $points", fontFamily = klimaFont, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = {
                            viewModel.resetQuiz()
                            navController.popBackStack()
                        }) {
                            Text("Tilbage", fontFamily = klimaFont)
                        }
                    }
                }

                else -> {
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
}
