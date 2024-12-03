package com.temple.aldwairi_projects_emotionecho.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedEmotionField(emotion: String) {
    // Box to hold the animated field
    val color = getEmotionColor(emotion)
    Box(
        modifier = Modifier
            .animateContentSize() // Smoothly animates size changes
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Circle to represent the emotion's color
            Canvas(modifier = Modifier.size(24.dp)) {
                drawCircle(color)
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Text to display the emotion label
            Text(
                text = emotion,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
    }
}
// Function to get color based on emotion (you can customize this further)
fun getEmotionColor(emotion: String): Color {
    return when (emotion) {
        "Calm" -> Color.Cyan
        "Happy" -> Color.Yellow
        "Sad" -> Color.Blue
        "Angry" -> Color.Red
        "Neutral" -> Color.Gray
        "Fearful" -> Color(255, 131, 0)
        "Disgust" -> Color.Green
        "Surprised" -> Color(0xFF800080)
        else -> Color.White
    }
}

@Preview
@Composable
fun PreviewAnimatedEmotionField(){
    AnimatedEmotionField("sad")
}