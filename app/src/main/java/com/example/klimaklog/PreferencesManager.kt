package com.example.klimaklog

// Mike

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "klima_preferences")

class PreferencesManager(private val context: Context) {
    companion object {
        val KLIMA_POINTS = intPreferencesKey("klima_points")
    }

    fun getKlimaPoints(): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[KLIMA_POINTS] ?: 0
        }
    }

    suspend fun setKlimaPoints(points: Int) {
        context.dataStore.edit { prefs ->
            prefs[KLIMA_POINTS] = points
        }
    }
}
