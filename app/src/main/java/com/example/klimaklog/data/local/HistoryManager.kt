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

// vi gemmer brugerens søgehistorik lokalt i DataStore
// alt kører i sustend, så intet blokerer UI
// Listen bliver gemt  som ét JSON-felt

// Giver hvert Context-objekt sit eget DataStore med navnet "klima_history".
val Context.historyStore by preferencesDataStore("klima_history")

class HistoryManager(private val context: Context) {

    // Nøgle til feltet hvor hele historikken (som JSON-streng) ligger.
    private val HISTORY_KEY = stringPreferencesKey("search_history")
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true }


 // Gemmer et nyt søgeresultat forrest i listen (senest først)
    suspend fun saveSearch(item: SearchHistoryItem) {
        val current = loadHistory().toMutableList() // hent nuværende liste
        current.add(0, item) // nyeste bliver placeret øverst
        val jsonString = json.encodeToString(current)
        context.historyStore.edit { it[HISTORY_KEY] = jsonString }
    }

    // Henter hele historikken
    // Returnerer en tom liste ved korrupt data i stedet for at crach appen
    suspend fun loadHistory(): List<SearchHistoryItem> {
        return try {
            val stored = context.historyStore.data.first()[HISTORY_KEY] ?: return emptyList()
            json.decodeFromString(stored)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // sletter hele historikken. O(1)
    suspend fun clearHistory() {
        context.historyStore.edit { it.remove(HISTORY_KEY) }
    }

    // sletter ét element fra historikken. bruger O(n) removeAt(index)
    suspend fun deleteItem(index: Int) {
        val current = loadHistory().toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            val jsonString = json.encodeToString(current)
            context.historyStore.edit { it[HISTORY_KEY] = jsonString }
        }
    }
}
