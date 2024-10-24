package com.temple.aldwairi_projects_emotionecho.ui.components



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.temple.aldwairi_projects_emotionecho.ui.theme.TempleCherryRed
import com.temple.aldwairi_projects_emotionecho.ui.theme.TempleYellow

/**
 * Creates a custom Button Composable
 * @param text the text you want shown on the button
 * @param brush requires a List of at least 2 Colors, used to make the button look more pretty
 * @param onClick callback function for Button's onclick property
 */
@Composable
fun CustomButton(
    text: String = "Button",
    brush: List<Color> = listOf(Black, TempleYellow, TempleCherryRed, TempleCherryRed,
        TempleCherryRed, TempleYellow, Black), // I know these default colors are kinda ridiculous, will change later
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .background(
                brush = Brush.horizontalGradient(brush),
                shape = RoundedCornerShape(25.dp)
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun PreviewButton(){
    CustomButton("This is a Button") { }
}
