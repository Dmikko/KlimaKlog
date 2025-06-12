package com.example.klimaklog.data.remote

// HC

import com.example.klimaklog.BuildConfig
import com.example.klimaklog.model.SearchHistoryItem
import com.example.klimaklog.model.QuizQuestion
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
    Du er en klimaquiz-master for børn og unge i 8. klasse til 1.g. Du skal lave ét multiple 
    choice-spørgsmål, som handler om klima, bæredygtighed eller CO₂-udledning – aldrig om andre emner.

    Brug det givne emne: "${item.query}"

    Svar altid i dette præcise format (uden tal, uden punktummer eller ekstra tegn):

    Spørgsmål: [dit spørgsmål her]

    A) [forkert svar]

    B) [rigtigt svar] ✅

    C) [forkert svar]

    Tilføj en relevant emoji foran det ord eller emne, spørgsmålet handler om (fx 🌭hotdog, 
    👕T-shirt, 🍕pizza, 🧃juice osv.).

    Brug let og børnevenligt sprog. Undgå svære fagord og hold tonen positiv.
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
