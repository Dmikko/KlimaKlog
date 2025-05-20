package com.example.klimaklog

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.klimaklog.ai.getClimateInfoFromQuery
import com.example.klimaklog.data.HistoryManager
import com.example.klimaklog.data.SearchHistoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, query: String) {
    val klimaFont = FontFamily(Font(R.font.jolly_lodger))
    val context = LocalContext.current
    var aiResponse by remember { mutableStateOf("Indlæser...") }

    LaunchedEffect(query) {
        aiResponse = try {
            val result = getClimateInfoFromQuery(query)
            HistoryManager(context).saveSearch(SearchHistoryItem(query, result))
            result
        } catch (e: Exception) {
            "Noget gik galt. Prøv igen senere."
        }
    }

    val cards = remember(aiResponse) {
        aiResponse.split("\n\n").take(4)
    }


    val cardTitles = listOf(
        "CO₂-aftryk",
        "Hvad påvirker det?",
        "Sådan kan du reducere det",
        "Fun fact"
    )

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
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("search") },
                    label = { Text("Søgning", fontFamily = klimaFont) },
                    icon = {}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("quiz") },
                    label = { Text("Quiz", fontFamily = klimaFont) },
                    icon = {}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("history") },
                    label = { Text("Historik", fontFamily = klimaFont) },
                    icon = {}
                )
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
                text = "AI-svar om: \"$query\"",
                fontSize = 30.sp,
                fontFamily = klimaFont,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            cards.forEachIndexed { index, text ->
                ResultCard(
                    title = cardTitles.getOrNull(index) ?: "",
                    content = text.trim(),
                    font = klimaFont
                )
            }
        }
    }
}

