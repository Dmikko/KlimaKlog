package com.example.klimaklog.ai

// HC

import com.example.klimaklog.BuildConfig
import com.example.klimaklog.data.SearchHistoryItem
import com.example.klimaklog.quiz.model.QuizQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

suspend fun generatePersonalQuizFromHistory(history: List<SearchHistoryItem>): List<QuizQuestion> = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val resultList = mutableListOf<QuizQuestion>()

    for (item in history) {
        val systemPrompt = """
            Du er en klimaquiz-master for børn og unge. Lav ét multiple choice spørgsmål ud fra dette klimaemne:
            "${item.query}"
            
            
            Formatér hvert spørgsmål præcis som dette (uden tal eller ekstra tegn foran, men Tilføj tilsvarende emoji før navnet(f.eks. 🧃Juice, 🌭hotdog, 🍕pizzeslice, 👕Tshirt etc)):
            Spørgsmål: ...
            A) ...
            B) ... ✅
            C) ...
        """.trimIndent()

        val userPrompt = "Lav et spørgsmål om: ${item.query}"

        val json = """
            {
              "model": "gpt-4o",
              "messages": [
                { "role": "system", "content": ${JSONObject.quote(systemPrompt)} },
                { "role": "user", "content": ${JSONObject.quote(userPrompt)} }
              ],
              "temperature": 0.5
            }
        """.trimIndent()

        val body = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(body)
            .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
            .build()

        val response = client.newCall(request).execute()
        val bodyString = response.body?.string() ?: continue
        val raw = JSONObject(bodyString)
            .getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content")

        // Parsing af ét spørgsmål
        val lines = raw.trim().lines()
        val questionText = lines.firstOrNull { it.startsWith("Spørgsmål:") }?.removePrefix("Spørgsmål:")?.trim() ?: continue
        val options = lines.drop(1).filter { it.startsWith("A)") || it.startsWith("B)") || it.startsWith("C)") }
        val parsedOptions = options.map { it.drop(2).replace("✅", "").trim() }
        val correct = options.find { it.contains("✅") }?.drop(2)?.replace("✅", "")?.trim() ?: parsedOptions.firstOrNull() ?: continue

        if (parsedOptions.size == 3 && correct in parsedOptions) {
            resultList.add(
                QuizQuestion(
                    text = questionText,
                    options = parsedOptions,
                    correctAnswer = correct,
                    level = "personal"
                )
            )
        }
    }

    return@withContext resultList
}
