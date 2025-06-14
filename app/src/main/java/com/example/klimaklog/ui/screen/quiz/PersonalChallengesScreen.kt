package com.example.klimaklog.ui.screen.quiz

// HC og Mike

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.klimaklog.R
import com.example.klimaklog.data.remote.generatePersonalQuizFromHistory
import com.example.klimaklog.data.local.HistoryManager
import com.example.klimaklog.viewmodel.QuizViewModel
import com.example.klimaklog.ui.components.QuizQuestionUI
import com.example.klimaklog.ui.theme.klimaFontTitle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalChallengesScreen(navController: NavController, viewModel: QuizViewModel = viewModel()) {
    val klimaFont = remember { FontFamily(Font(R.font.jolly_lodger)) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val question by viewModel.currentQuestion.collectAsState()
    val userAnswer by viewModel.userAnswer.collectAsState()
    val points by viewModel.points.collectAsState()


    var isQuizFinished by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val history = HistoryManager(context).loadHistory()
                if (history.isEmpty()) {
                    errorMessage = "Du har endnu ikke stillet spørgsmål."
                } else {
                    val generatedQuestions = generatePersonalQuizFromHistory(history)
                    println("Antal spørgsmål genereret: ${generatedQuestions.size}")

                    if (generatedQuestions.isEmpty()) {
                        errorMessage = "AI kunne ikke generere din personlige quiz."
                    } else {
                        viewModel.loadCustomQuestions(generatedQuestions)
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Noget gik galt: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personlig Quiz", fontFamily = klimaFontTitle, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tilbage")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            when {
                isLoading -> {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("AI laver din quiz...", fontFamily = klimaFontTitle)
                    }
                }

                errorMessage != null -> {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(errorMessage ?: "", fontFamily = klimaFont, fontSize = 16.sp)
                    }
                }

                isQuizFinished -> {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Du har gennemført den personlige quiz!", fontFamily = klimaFontTitle, fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Personlige point: $points", fontFamily = klimaFontTitle, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                viewModel.resetQuiz()
                                navController.popBackStack()
                            },
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text("Tilbage", fontFamily = klimaFontTitle)
                        }
                    }
                }

                question != null -> {
                    QuizQuestionUI(
                        question = question!!,
                        userAnswer = userAnswer,
                        points = points,
                        onAnswerSelected = { viewModel.submitAnswer(it) },
                        onNext = {
                            val hasNext = viewModel.nextQuestion()
                            if (!hasNext) isQuizFinished = true
                        },
                        font = klimaFontTitle
                    )
                }

                else -> {
                    Text("Ingen spørgsmål fundet.", fontFamily = klimaFontTitle, modifier = Modifier.padding(24.dp))
                }
            }
        }
    }
}
