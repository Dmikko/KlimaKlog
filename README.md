(AI genereret README)

# 🌱 Klima Klog – En læringsapp om klima og bæredygtighed

**Klima Klog** er en Android-app udviklet som eksamensprojekt på 2. semester på IT-arkitekturlinjen ved KEA. Appen er målrettet undervisning i “Vores Klimaplan” og henvender sig til elever i 8. klasse til 1.g. Formålet er at gøre klimaviden konkret og engagerende gennem en kombination af fritekstsøgning, AI-integration og quizbaseret læring.

---

## 📲 Funktioner

- **Fritekstsøgning med AI**  
  Eleven kan søge på et valgfrit klimaemne, f.eks. "tøj" eller "mad", og modtage AI-genererede svar struktureret i temaer.

- **Historik og refleksion**  
  Gemte søgninger kan vises som lister, bruges til opsamling i klassen eller danne grundlag for personlig refleksion.

- **Quiz med fast og dynamisk indhold**  
  Appen rummer både faste quizspørgsmål og dynamisk genererede spørgsmål baseret på brugerens søgninger.

- **Offline fallback**  
  Hvis der ikke er netforbindelse, skifter appen automatisk til offline quiz med 60 forudindlæste spørgsmål.

---

## ⚙️ Teknologi og arkitektur

- **Sprog:** Kotlin  
- **UI Framework:** Jetpack Compose  
- **Arkitektur:** MVVM  
- **State-håndtering:** MutableStateFlow  
- **Navigation:** Enum-baseret custom navigation  
- **AI-integration:** OpenAI API (`gpt-3.5-turbo`) via Retrofit  
- **Persistent storage:** SharedPreferences  
- **Build & Test Tools:** GitHub Actions, ktlint, detekt, unit tests

---

## 🧠 “Sej kode” – Eksempler

- **AI-prompt engineering**  
  Systemprompts formaterer svar i 4 pædagogiske sektioner. Dynamisk prompt-generering i `AIService.kt`.

- **Fejlrobust quizgenerering**  
  Hver AI-genereret quizopgave kaldes separat for at sikre JSON-validitet og fallback-logik.

- **Offline fallback-strategi**  
  Automatisk skift til lokale spørgsmål ved netværksfejl – vigtig funktionalitet i klassekontekst.

---

## 🧪 Kvalitetssikring

- Manuel og automatisk test på emulator og fysisk enhed  
- UI-test af klikflows, navigation og brugerfeedback  
- Review af funktioner og komponenter i gruppen  
- CI pipeline med lint, build og tests via GitHub Actions  
- Dokumentation af kode med `KDoc`

---

## 🧭 Mappestruktur (uddrag)

📦 Klimaklog/
├── data/
│ ├── local/
│ ├── remote/
├── model/
├── ui/
│ ├── screen/
│ ├── components/
├── viewmodel/
├── theme/
├── assets/
│ └── quiz_questions.json


---

## 🚀 Kom i gang

1. **Krav**  
   Android Studio (Hedgehog eller nyere)  
   Android SDK 24+  
   OpenAI API Key (placeres i `local.properties` som `openai.api.key`)

2. **Klon projektet**
   ```bash
   git clone https://github.com/Dmikko/KlimaKlog.git
Kør projektet
Åbn i Android Studio og start direkte på emulator eller fysisk enhed.

📚 Rapport og baggrund
Dette projekt er beskrevet i detaljer i vores eksamensrapport “Vores Klimaplan – Fra indsigt til app”, som findes i repoet som PDF. Rapporten dækker hele processen fra brugerresearch til teknisk implementering.

🧑‍🤝‍🧑 Projektgruppe
Udviklet af 4 studerende på KEA – IT-Arkitektur 2. semester (F25):

Esben

Johan

Mike

Mathias

📝 Licens
Dette projekt er open source og tilgængeligt under MIT-licensen.
