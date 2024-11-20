package com.temple.aldwairi_projects_emotionecho.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomDividerWithText(){
    Row(
        modifier = Modifier.fillMaxWidth().padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().weight(1F),
            color = Color.Gray,
            thickness = 2.dp
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text= "or",
            fontSize = 16.sp,
            color = Color.Gray)

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().weight(1F),
            color = Color.Gray,
            thickness = 2.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomDividerWithText(){
    CustomDividerWithText()
}