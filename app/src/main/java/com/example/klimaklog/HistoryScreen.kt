package com.example.klimaklog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.klimaklog.data.HistoryManager
import com.example.klimaklog.data.SearchHistoryItem
import com.example.klimaklog.ui.theme.klimaFont

@Composable
fun HistoryScreen(navController: NavController) {
    val context = LocalContext.current
    var history by remember { mutableStateOf<List<SearchHistoryItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        history = HistoryManager(context).loadHistory()
    }



    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
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
    ) { paddingValues ->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
        ) {
        Text("Søgehistorik", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))

            if (history.isEmpty()) {
                Text("Ingen historik endnu.")
            } else {
                LazyColumn {
                    items(history) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Spørgsmål: ${item.query}", fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(4.dp))
                                Text(item.response.take(200) + "...")
                            }
                        }
                    }
                }
            }
        }
    }

}





