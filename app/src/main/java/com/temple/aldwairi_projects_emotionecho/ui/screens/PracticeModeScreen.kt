package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeModeScreen(
    context: Context
){
    var isExpanded by remember { mutableStateOf(false) }
    var option by remember { mutableStateOf("") }

    var emotions = listOf("Happy", "Sad", "Angry", "Disgusted", "Fearful")

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            ExposedDropdownMenuBox(
                expanded = true,
                onExpandedChange = { isExpanded = !isExpanded }
            ) {
                TextField(
                    readOnly = true,
                    value = option,
                    onValueChange = {},
                    label = { Text("Select an element")},
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    for (emotion in emotions){
                        DropdownMenuItem(
                            text = {Text(emotion)},
                            onClick = {
                                option = emotion
                                isExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier=Modifier.height(50.dp))
            CustomButton(
                "SUBMIT RESPONSE",
                listOf(Color.Black, Color.Gray)
            ) {
                Toast.makeText(context, "You chose $option", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPracticeModeScreen(){
    // Use LocalContext if available, or a default fallback for Preview
    val mockContext = LocalContext.current
    PracticeModeScreen(mockContext)
}