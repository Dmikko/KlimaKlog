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

// PersonalQuizService tager listen af søgehistorik-items fra brugeren
// og giver den tilbage til AI, som så laver en personlig multiple-choice klima-quiz
// udfra hver spørgsmål, der er i søgehistorik


suspend fun generatePersonalQuizFromHistory(history: List<SearchHistoryItem>): List<QuizQuestion> = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val resultList = mutableListOf<QuizQuestion>()

    // loop gennem hvert historik-item
    for (item in history) {
        // definerer rolle, tone og format
        val systemPrompt = """
Du er en klimaquiz-master for børn og unge i 8. klasse til 1.g. Du skal lave ét multiple 
choice-spørgsmål, som handler om klima, bæredygtighed eller CO₂-udledning – aldrig om andre emner.

Brug det givne emne: "${item.query}"

Svar altid i præcis dette format – uden ekstra tegn, uden punktummer, og med én korrekt svarmulighed:

Spørgsmål: [dit spørgsmål her]

A) ...
B) ...
C) ...

Markér det **korrekte svar** med symbolet ✅ – og placer det tilfældigt som A), B) eller C)

Eksempel:
A) Forkert svar  
B) Korrekt svar ✅  
C) Forkert svar

Tilføj en emoji der passer til emnet, f.eks. 🌭hotdog, 👕T-shirt, 🍕pizza – men kun én emoji, og placer den først i spørgsmålet.

Skriv i børnevenligt og let sprog, og undgå svære fagudtryk.
""".trimIndent()



        // userPrompt sætter selve emnet for hvert spørgsmål
        val userPrompt = "Lav et spørgsmål om: ${item.query}"

        // HTTP-request til OpenAI
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

        // Præcis 3 svarmuligheder med ét korrekt svar
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

    // Færdigliste returneres til UI-laget.
    return@withContext resultList
}
