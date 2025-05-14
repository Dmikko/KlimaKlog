package com.example.klimaklog.quiz

import BottomNavigationItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.klimaklog.ui.theme.klimaFont

@Composable
fun QuizOverviewScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomNavigationItem(navController, "search", "Søgning", klimaFont)
                BottomNavigationItem(navController, "quiz", "Quiz", klimaFont)
                BottomNavigationItem(navController, "history", "Historik", klimaFont)
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

            // Titel
            Text(
                text = "Klima Klog\nQuiz",
                style = TextStyle(fontFamily = klimaFont, fontSize = 44.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Beskrivelse
            Text(
                text = "Test din viden om klima og CO₂ – tjen point og bliv en klimahelt!",
                style = TextStyle(fontFamily = klimaFont, fontSize = 16.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Knapper til quizniveauer
            QuizButton("Let", klimaFont) { navController.navigate("quiz/let") }
            Spacer(modifier = Modifier.height(24.dp))
            QuizButton("Mellem", klimaFont) { navController.navigate("quiz/mellem") }
            Spacer(modifier = Modifier.height(24.dp))
            QuizButton("Svær", klimaFont) { navController.navigate("quiz/svaer") }

            Spacer(modifier = Modifier.height(40.dp))

            // Knap til personlige challenges
            QuizButton("Personlige Challenges", klimaFont) {
                navController.navigate("quiz/personal")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Pointvisning
            Surface(
                color = Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(50),
                shadowElevation = 4.dp
            ) {
                Text(
                    text = "Klima Points\n120",
                    fontFamily = klimaFont,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

