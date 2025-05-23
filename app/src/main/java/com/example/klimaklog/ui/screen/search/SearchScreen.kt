package com.example.klimaklog.ui.screen.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.klimaklog.R

@Composable
fun SearchScreen(navController: NavController) {
    val klimaFont = try {
        FontFamily(Font(R.font.jolly_lodger))
    } catch (e: Exception) {
        FontFamily.Default
    }
    var query by remember { mutableStateOf("") }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Klima Klog",
                style = TextStyle(fontFamily = klimaFont, fontSize = 40.sp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Hvad vil du gerne klima-vide noget mere om?",
                style = TextStyle(fontFamily = klimaFont, fontSize = 18.sp)
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
                Text("Søg", fontSize = 20.sp, fontFamily = klimaFont)
            }
        }
    }
}