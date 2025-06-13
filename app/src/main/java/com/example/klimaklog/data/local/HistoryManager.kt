package com.example.klimaklog.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.klimaklog.model.SearchHistoryItem
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


//Mike

val Context.historyStore by preferencesDataStore("klima_history")

class HistoryManager(private val context: Context) {
    private val HISTORY_KEY = stringPreferencesKey("search_history")
    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }

    suspend fun saveSearch(item: SearchHistoryItem) {
        val current = loadHistory().toMutableList()
        current.add(0, item)
        val jsonString = json.encodeToString(current)
        context.historyStore.edit { it[HISTORY_KEY] = jsonString }
    }

    suspend fun loadHistory(): List<SearchHistoryItem> {
        return try {
            val stored = context.historyStore.data.first()[HISTORY_KEY] ?: return emptyList()
            json.decodeFromString(stored)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun clearHistory() {
        context.historyStore.edit { it.remove(HISTORY_KEY) }
    }

    suspend fun deleteItem(index: Int) {
        val current = loadHistory().toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            val jsonString = json.encodeToString(current)
            context.historyStore.edit { it[HISTORY_KEY] = jsonString }
        }
    }

}
