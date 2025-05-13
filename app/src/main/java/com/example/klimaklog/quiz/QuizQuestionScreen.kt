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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.klimaklog.R
import kotlinx.coroutines.delay


data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)

val quizQuestionsEasy = listOf(
    QuizQuestion("Hvor meget CO₂ udleder det cirka at producere en almindelig bomulds-t-shirt?", listOf("0,3 kg", "2–4 kg", "9 kg", "15 kg"), 1)
)

@Composable
fun QuizQuestionScreen(navController: NavController, difficulty: String, font: FontFamily) {
    val question = remember { quizQuestionsEasy.random() } // senere: afhængig af 'difficulty'
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var answered by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(question.question, fontFamily = font, fontSize = 18.sp, modifier = Modifier
            .background(Color(0xFFA9F9C3), RoundedCornerShape(24.dp))
            .padding(16.dp))

        Spacer(Modifier.height(24.dp))

        question.options.forEachIndexed { index, answer ->
            Button(
                onClick = {
                    selectedIndex = index
                    answered = true
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.8f),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA9F9C3))
            ) {
                Text(answer, fontFamily = font)
            }
        }

        if (answered) {
            val correct = selectedIndex == question.correctIndex
            LaunchedEffect(Unit) {
                delay(1000)
                navController.navigate("quiz_result/$correct")
            }
        }
    }
}
