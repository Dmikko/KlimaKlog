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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimateChallengesScreen(navController: NavController) {
    val klimaFont = FontFamily(Font(R.font.jolly_lodger))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Klima Klog Quiz", fontFamily = klimaFont) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tilbage")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Vælg en sværhedsgrad og test din klimaviden!",
                fontFamily = klimaFont,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            DifficultyButton("Let", Color(0xFFB6F8C2), klimaFont) {
                navController.navigate("quiz_easy")
            }

            DifficultyButton("Mellem", Color(0xFFF6F49D), klimaFont) {
                navController.navigate("quiz_medium")
            }

            DifficultyButton("Svær", Color(0xFFF8B6B6), klimaFont) {
                navController.navigate("quiz_hard")
            }
        }
    }
}

@Composable
fun DifficultyButton(label: String, bgColor: Color, font: FontFamily, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 8.dp)
    ) {
        Text(label, fontFamily = font, fontSize = 22.sp)
    }
}
