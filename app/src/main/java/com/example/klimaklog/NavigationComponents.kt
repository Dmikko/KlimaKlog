package com.example.klimaklog

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController, font: FontFamily) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("search") },
            label = { Text("Søgning", fontFamily = font) },
            icon = {}
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("quiz") },
            label = { Text("Quiz", fontFamily = font) },
            icon = {}
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("history") },
            label = { Text("Historik", fontFamily = font) },
            icon = {}
        )
    }
}
