package com.example.klimaklog.ui.screen.history

// HC

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.klimaklog.data.local.HistoryManager
import com.example.klimaklog.model.SearchHistoryItem
import com.example.klimaklog.ui.theme.klimaFont
import com.example.klimaklog.ui.theme.klimaFontTitle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    val context = LocalContext.current
    var history by remember { mutableStateOf<List<SearchHistoryItem>>(emptyList()) }
    var expandedCardIndex by remember { mutableStateOf<Int?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        history = HistoryManager(context).loadHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Søgehistorik", fontFamily = klimaFontTitle) },
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
                    label = { Text("Søgning", fontFamily = klimaFontTitle, fontSize = 28.sp) },
                    icon = {}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("quiz") },
                    label = { Text("Quiz", fontFamily = klimaFontTitle, fontSize = 28.sp) },
                    icon = {}
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { navController.navigate("history") },
                    label = { Text("Historik", fontFamily = klimaFontTitle, fontSize = 28.sp) },
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
                        val isExpanded = expandedCardIndex == index

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    expandedCardIndex = if (isExpanded) null else index
                                },
                        ) {
                            //Hvorfor kører vi anden font type her?
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Spørgsmål: ${item.query}", fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = if (isExpanded) item.response else item.response.take(150) + "...",
                                    style = MaterialTheme.typography.bodyMedium
                                )
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
