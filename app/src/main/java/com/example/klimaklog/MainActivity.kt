package com.example.klimaklog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.klimaklog.ui.theme.KlimaKlogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KlimaKlogTheme { // Custom theme. Ellers brug MaterialTheme direkte.
                Surface(color = MaterialTheme.colorScheme.background) {
                    KlimaNavGraph() // VIGTIGT: Starter vores navigation
                }
            }
        }
    }
}
