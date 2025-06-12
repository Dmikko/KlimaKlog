package com.example.klimaklog.ui.screen.quiz

// HC og Mike

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.klimaklog.ui.components.QuizButton
import com.example.klimaklog.viewmodel.QuizViewModel
import com.example.klimaklog.ui.theme.klimaFont
import com.example.klimaklog.ui.theme.klimaFontTitle

@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel = viewModel()
) {
    val totalPoints by viewModel.totalPoints.collectAsState()
    val personalPoints by viewModel.personalPoints.collectAsState()
    val klimaPoints = totalPoints - personalPoints
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("search") },
                    label = { Text("Søgning", fontFamily = klimaFont, fontSize = 24.sp) },
                    icon = {}
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { navController.navigate("quiz") },
                    label = { Text("Quiz", fontFamily = klimaFont, fontSize = 24.sp) },
                    icon = {}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("history") },
                    label = { Text("Historik", fontFamily = klimaFont, fontSize = 24.sp) },
                    icon = {}
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tilbageknap
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

            Text(
                text = "Klima Klog\nQuiz",
                style = TextStyle(fontFamily = klimaFontTitle, fontSize = 44.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Test din viden om klima og CO₂ – tjen point og bliv en klimahelt!",
                style = TextStyle(fontFamily = klimaFontTitle, fontSize = 16.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            QuizButton("Klima Challenges", klimaFontTitle) {
                navController.navigate("quiz/overview")
            }

            Spacer(modifier = Modifier.height(24.dp))

            QuizButton("Personlige Challenges", klimaFontTitle) {
                navController.navigate("quiz/personal")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Pointboks – klikbar
            Surface(
                color = Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(50),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { showResetDialog = true }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Klima Quiz: $klimaPoints", fontFamily = klimaFont, fontSize = 14.sp)
                    Text("Personlig Quiz: $personalPoints", fontFamily = klimaFont, fontSize = 14.sp)
                    Text("I alt: $totalPoints", fontFamily = klimaFont, fontSize = 16.sp)
                }
            }
        }
    }

    // Nulstil point popup
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Nulstil point?", fontFamily = klimaFont) },
            text = { Text("Er du sikker på, at du vil slette alle dine point?", fontFamily = klimaFont) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetAllPoints()
                    showResetDialog = false
                }) {
                    Text("Ja", fontFamily = klimaFont)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Annullér", fontFamily = klimaFont)
                }
            }
        )
    }
}
