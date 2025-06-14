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

//Mathias

// AIService sender brugerens spørgsmål til OpenAI API
// og returnerer et letforståeligt klima-svar med CO2-aftryk,
// hvad påvirker det, sådan kan du reducere det og fun fact


suspend fun getClimateInfoFromQuery(query: String): String = withContext(Dispatchers.IO) {
    val client = OkHttpClient() // Én klient til HTTP-kaldet

    // systemPromt definere rolle, tonen og svarets præcise format
    val systemPrompt = """
    Du er en hjælpsom og pædagogisk klimaassistent for børn og unge i 8. klasse til 1.g. Dit job er 
    at forklare klimaaftryk og bæredygtighed på en positiv og forståelig måde, når de spørger om et 
    produkt, en aktivitet eller et emne.

    Du skal altid svare i præcis dette format – uden overskrifter, uden ekstra forklaringer – kun 
    de fire punkter herunder i nævnte rækkefølge, adskilt af to linjeskift (\n\n):

    1. CO2-aftryk: Forklar kort, hvor meget CO₂ der udledes – gerne med tal og sammenligninger.
    
    2. Hvad påvirker det?: Beskriv hvad i produktets livscyklus eller produktion der har størst 
    effekt på klimaet.
    
    3. Sådan kan du reducere det: Giv 1–2 simple forslag til grønnere alternativer eller valg.
    
    4. Fun fact: Slut af med en sjov, positiv eller opmuntrende ekstra viden.

    Tilføj én relevant emoji i starten af svaret, som passer bedst muligt til det, brugeren spørger 
    om (fx 🌭 til hotdog, 🧃 til juice, 👕 til t-shirt, 🍕 til pizza, 🧼 til sæbe).

    Svar altid kort og i børnevenligt sprog. Undgå svære ord og forklar ting enkelt.
""".trimIndent()

    // bruger prompt
    val userPrompt = "Hvor meget CO₂ udleder: $query?"

    // JSON body til OpenAI
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

    // HTTP-request
    val request = Request.Builder()
        .url("https://api.openai.com/v1/chat/completions")
        .post(body)
        .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
        .build()

    // udfør og log svaret
    val response = client.newCall(request).execute()
    val responseBody = response.body?.string()
    Log.d("AIService", "HTTP status: ${response.code}")
    Log.d("AIService", "Raw response: $responseBody")
    if (!response.isSuccessful || responseBody == null) {
        Log.e("OpenAI_API_ERROR", "Fejl ved forespørgsel: ${response.code}")
        throw Exception("Fejl ved OpenAI API")
    }

    // trækker tekst-svaret ud af JSON
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