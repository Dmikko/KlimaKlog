package com.example.klimaklog.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.klimaklog.ui.theme.klimaFont

@Composable
fun PersonalChallengesScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Personlige Challenges", fontSize = 28.sp, fontFamily = klimaFont)
        Spacer(Modifier.height(16.dp))
        Text("Her kommer dine dynamiske AI-genererede spørgsmål.", fontSize = 16.sp)
        // TODO: Tilføj OpenAI-integration og historikbaseret prompt senere
    }
}
