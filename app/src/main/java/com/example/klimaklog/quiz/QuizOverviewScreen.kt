package com.example.klimaklog.quiz

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

@Composable
fun QuizOverviewScreen(navController: NavController, font: FontFamily) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController, font) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Klima Klog\nQuiz", fontSize = 40.sp, fontFamily = font)
            Text("Test din viden om klima og CO₂ – tjen point og bliv en klimahelt!!", fontSize = 14.sp, fontFamily = font)

            Spacer(Modifier.height(24.dp))

            QuizDifficultyButton("Let") { navController.navigate("quiz_question/easy") }
            QuizDifficultyButton("Mellem") { navController.navigate("quiz_question/medium") }
            QuizDifficultyButton("Svær") { navController.navigate("quiz_question/hard") }
        }
    }
}

@Composable
fun BottomNavigationBar(x0: NavController, x1: FontFamily) {
    TODO("Not yet implemented")
}

@Composable
fun QuizDifficultyButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA9F9C3))
    ) {
        Text(text, fontSize = 20.sp, fontFamily = FontFamily.Default)
    }
}
