package com.example.klimaklog.data.remote

import android.util.Log
import com.example.klimaklog.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


suspend fun getClimateInfoFromQuery(query: String): String = withContext(Dispatchers.IO) {
    val client = OkHttpClient()

    val systemPrompt = """
        Du er en klimaassistent for børn i 8.–1.g. klasse. Når de spørger om et produkt/aktivitet, skal du svare med følgende format (uden overskrifter):

        1. CO2-aftryk: Forklar kort og med tal hvor meget CO₂ udledes.
        2. Hvad påvirker det?: Fortæl hvad der bidrager mest til aftrykket.
        3. Sådan kan du reducere det: Giv forslag til grønnere valg.
        4. Fun fact: En sjov, positiv ekstra information.
        
        Tilføj tilsvarende emojis til hver spørgsmål, brugeren stiller (f.eks. 🧃Juice, 🌭hotdog, 🍕pizzeslice, 👕Tshirt etc)
        

        Brug to nye linjer (\n\n) mellem hvert afsnit.
    """.trimIndent()

    val userPrompt = "Hvor meget CO₂ udleder: $query?"

    val json = """
        {
          "model": "gpt-4o",
          "messages": [
            { "role": "system", "content": ${JSONObject.quote(systemPrompt)} },
            { "role": "user", "content": ${JSONObject.quote(userPrompt)} }
          ],
          "temperature": 0.3
        }
    """.trimIndent()

    val body = json.toRequestBody("application/json".toMediaType())

    val request = Request.Builder()
        .url("https://api.openai.com/v1/chat/completions")
        .post(body)
        .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
        .build()

    val response = client.newCall(request).execute()
    val responseBody = response.body?.string()
    Log.d("AIService", "HTTP status: ${response.code}")
    Log.d("AIService", "Raw response: $responseBody")
    if (!response.isSuccessful || responseBody == null) {
        Log.e("OpenAI_API_ERROR", "Fejl ved forespørgsel: ${response.code}")
        throw Exception("Fejl ved OpenAI API")
    }

    return@withContext try {
        JSONObject(responseBody)
            .getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content")
            .trim()
    } catch (e: Exception) {
        Log.e("OpenAI_API_ERROR", "Fejl under parsing", e)
        throw Exception("OpenAI returnerede ikke gyldigt svar.")
    }
}