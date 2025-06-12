package com.example.klimaklog.ui.screen.result

// HC og Mike

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
import com.example.klimaklog.R
import com.example.klimaklog.ui.components.ResultCard
import com.example.klimaklog.data.remote.getClimateInfoFromQuery
import com.example.klimaklog.data.local.HistoryManager
import com.example.klimaklog.model.SearchHistoryItem
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

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
        if (!aiResponse.contains("\n\n")) return@remember emptyList()

        aiResponse
            .split("\n\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .let { chunks ->
                if (chunks.firstOrNull()
                        ?.startsWith("CO", ignoreCase = true) == true
                ) chunks else chunks.drop(1)
            }
            .take(4)
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "AI-svar om: \"$query\"",
                fontSize = 30.sp,
                fontFamily = klimaFont,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // Skrevet med hjælp fra ChatGPT
            if (cards.isEmpty()) {
                // Stadig i gang med at hente / parse
                Text(
                    text = "Indlæser…",
                    fontSize = 20.sp,
                    fontFamily = klimaFont,
                    modifier = Modifier.padding(top = 24.dp)
                )
                CircularProgressIndicator(modifier = Modifier.padding(top = 12.dp))
            } else {
                // Vi har data – vis kortene
                cards.forEachIndexed { index, rawText ->
                    val body = rawText.substringAfter(":", rawText).trim()

                    ResultCard(
                        title   = cardTitles.getOrNull(index) ?: "",
                        content = body,
                        font    = klimaFont
                    )
                }
            }


        }
    }
}

