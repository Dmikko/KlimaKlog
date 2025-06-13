package com.example.klimaklog.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klimaklog.model.QuizQuestion

//Esben Mike

@Composable
fun QuizQuestionUI(
    question: QuizQuestion,
    userAnswer: String?,
    points: Int,
    onAnswerSelected: (String) -> Unit,
    onNext: () -> Unit,
    font: FontFamily
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = question.text,
            fontFamily = font,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        question.options.forEach { option ->
            Button(
                onClick = { onAnswerSelected(option) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBFFFB8)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                Text(option, fontFamily = font, fontSize = 18.sp, color = Color.Black)
            }
        }

        if (userAnswer != null) {
            Spacer(modifier = Modifier.height(24.dp))
            val correct = userAnswer == question.correctAnswer
            Text(
                text = if (correct) "Korrekt! +10 point" else "Forkert 😢",
                color = if (correct) Color(0xFF4CAF50) else Color.Red,
                fontFamily = font,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Point: $points",
            fontFamily = font,
            fontSize = 18.sp,
            color = Color.DarkGray
        )

        if (userAnswer != null) {
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Næste", fontFamily = font, fontSize = 18.sp, color = Color.White)
            }
        }



    }
}
