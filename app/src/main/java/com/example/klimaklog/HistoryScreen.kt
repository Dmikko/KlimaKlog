package com.example.klimaklog

// HC

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    val context = LocalContext.current
    var history by remember { mutableStateOf<List<SearchHistoryItem>>(emptyList()) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        history = HistoryManager(context).loadHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Søgehistorik", fontFamily = klimaFont) },
                actions = {
                    TextButton(onClick = {
                        scope.launch {
                            HistoryManager(context).clearHistory()
                            history = emptyList()
                        }
                    }) {
                        Text("Ryd alt", color = Color.Red)
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
                    selected = true,
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
            if (history.isEmpty()) {
                Text("Ingen historik endnu.")
            } else {
                LazyColumn {
                    itemsIndexed(history) { index, item ->
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
                                Spacer(Modifier.height(8.dp))
                                TextButton(
                                    onClick = {
                                        scope.launch {
                                            HistoryManager(context).deleteItem(index)
                                            history = HistoryManager(context).loadHistory()
                                        }
                                    }
                                ) {
                                    Text("Slet", color = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
