package com.example.klimaklog.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klimaklog.quiz.model.QuizQuestion
import androidx.compose.foundation.clickable



@Composable
fun QuizQuestionUI(
    question: QuizQuestion,
    userAnswer: String?,
    onAnswerSelected: (String) -> Unit,
    klimaFont: FontFamily
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question.text,
            fontFamily = klimaFont,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        question.options.forEach { option ->
            Button(
                onClick = { onAnswerSelected(option) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBFFFB8)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.8f)
            ) {
                Text(option, fontFamily = klimaFont)
            }
        }

        userAnswer?.let {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (userAnswer == question.correctAnswer) "Korrekt! +10" else "Forkert 😢",
                style = TextStyle(fontFamily = klimaFont, fontSize = 20.sp),
                color = if (userAnswer == question.correctAnswer) Color.Green else Color.Red
            )
        }
    }
}


@Composable
fun QuizButton(text: String, font: FontFamily, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick),
        color = Color(0xFFB2FFB2),
        shape = RoundedCornerShape(30.dp),
        shadowElevation = 6.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontFamily = font,
                fontSize = 24.sp
            )
        }
    }
}