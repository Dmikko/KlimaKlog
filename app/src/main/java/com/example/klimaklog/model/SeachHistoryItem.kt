package com.example.klimaklog.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchHistoryItem(
    val query: String,
    val response: String,
    val timestamp: Long = System.currentTimeMillis()
)