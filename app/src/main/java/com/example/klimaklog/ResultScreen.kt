package com.example.klimaklog

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, query: String) {
    val klimaFont = FontFamily(Font(R.font.jolly_lodger)) // Husk at fonten er korrekt tilføjet

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Klima Klog", fontFamily = klimaFont, fontSize = 28.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Klima Points", fontSize = 10.sp, fontFamily = klimaFont)
                        Text("120", fontSize = 16.sp, fontFamily = klimaFont)
                    }
                }
            )
        },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hvor meget CO₂ udleder en Cheeseburger?",
                style = TextStyle(fontFamily = klimaFont, fontSize = 20.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            ResultCard("CO₂-aftryk", "En almindelig cheeseburger udleder ca. 2.5–3.5 kg CO₂.\nDet svarer næsten til at køre 15 km i bil.", klimaFont)
            ResultCard("Hvad påvirker det?", "Kød (især oksekød) står for den største del af udledningen.\nBrødet, osten og transporten bidrager også.", klimaFont)
            ResultCard("Sådan kan du reducere det", "Vælg en burger med plantefars, drop osten eller spis burger sjældnere.\nDet gør en stor forskel!", klimaFont)
            ResultCard("Fun fact", "En planteburger udleder typisk under 0.5 kg CO₂\n– næsten 7 gange mindre end en med oksekød.", klimaFont)
        }
    }
}

@Composable
fun BottomNavigationItem(navController: NavController, s: String, s1: String, klimaFont: FontFamily) {

}

@Composable
fun ResultCard(title: String, content: String, font: FontFamily) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8FAD8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontFamily = font, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content, fontFamily = font, fontSize = 14.sp, textAlign = TextAlign.Center)
        }
    }
}
