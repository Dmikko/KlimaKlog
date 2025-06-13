package com.example.klimaklog.ui.screen.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.klimaklog.R
import com.example.klimaklog.ui.theme.klimaFontTitle

//Esben og Johan

@Composable
fun SearchScreen(navController: NavController) {
    val klimaFont = try {
        FontFamily(Font(R.font.roboto))
    } catch (e: Exception) {
        FontFamily.Default
    }

    var query by remember { mutableStateOf("") }

    // Ny state til at styre om popup vises
    var showDialog by remember { mutableStateOf(false) }

    // Hele skærmen
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
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
                    selected = false,
                    onClick = { navController.navigate("history") },
                    label = { Text("Historik", fontFamily = klimaFontTitle, fontSize = 28.sp) },
                    icon = {}
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Info-knap øverst til højre
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Hjælp"
                    )
                }
            }

            Text(
                text = "Klima Klog",
                style = TextStyle(fontFamily = klimaFontTitle, fontSize = 80.sp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Hvad vil du gerne klima-vide noget mere om?",
                style = TextStyle(fontFamily = klimaFontTitle, fontSize = 22.sp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Pølsehorn, t-shirts, el-biler eller?") },
                shape = RoundedCornerShape(50),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (query.isNotBlank()) navController.navigate("result/${query}")
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
            ) {
                Text("Søg", fontSize = 30.sp, fontFamily = klimaFontTitle)
            }
        }
    }

    // Forklarings-popup (AlertDialog)
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text("Hvordan fungerer det?")
            },
            text = {
                Text("Skriv noget som du vil lære mere om. " +
                        "Det kan være alt fra hvad du har spist i dag, " +
                        "hvor langt du har kørt i bil," +
                        "hvad for et program du vaskemaskine har kørt på, " +
                        "eller noget helt fjerde!.")
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Forstået")
                }
            }
        )
    }
}