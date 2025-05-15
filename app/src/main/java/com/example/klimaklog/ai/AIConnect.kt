package com.example.klimaklog.ai

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
        Du er en klimaassistent for børn i 5.–7. klasse. Når de spørger dig om et produkt eller en aktivitet (fx 'cola dåse', 'cykeltur', 't-shirt'), skal du:
        - Forklare klimaaftrykket i børnevenligt sprog (max 3 korte sætninger).
        - Skrive det i et venligt og opmuntrende tonefald.
        - Give realistiske tal på CO₂-udledning (gæt hvis nødvendigt).
        - Foreslå evt. et grønnere alternativ.
        - Afslut med en opfordring som fx “Godt tænkt!” eller “Klimaklogt valg!”
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


