package com.example.klimaklog.ui.screen.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun QuizResultScreen(navController: NavController, wasCorrect: Boolean, font: FontFamily) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            if (wasCorrect) "KORREKT!" else "FORKERT!",
            fontSize = 36.sp,
            fontFamily = font,
            color = if (wasCorrect) Color.Black else Color.Red
        )
        if (wasCorrect) Text("+10", fontSize = 24.sp, fontFamily = font)

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack("quiz_overview", false) },
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA9F9C3))
        ) {
            Text("Tilbage", fontFamily = font)
        }
    }
}
