package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chaquo.python.Python
import com.google.gson.Gson
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton
import com.temple.aldwairi_projects_emotionecho.ui.components.PieChartWithLegend


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealTimeModeScreen(
    context: Context,
    modifier: Modifier
){
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var option by rememberSaveable { mutableStateOf("") }
    val python = Python.getInstance()
    val microphoneChoice = listOf("internal","external")
    val floatList = rememberSaveable { mutableStateOf<List<Float>?>(null) }

    fun initializeFloatList(floatListParam: List<Float>){
        floatList.value = floatListParam
    }
    fun isInitialize(): Boolean{
        return floatList.value != null
    }

    
    Surface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            floatList.value?.let { PieChartWithLegend(it) }

            ExposedDropdownMenuBox(
                expanded = true,
                onExpandedChange = { isExpanded = !isExpanded }
            ) {
                TextField(
                    readOnly = true,
                    value = option,
                    onValueChange = {},
                    label = { Text("Select an microphone") },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    for (mic in microphoneChoice){
                        DropdownMenuItem(
                            text = { Text(mic) },
                            onClick = {
                                option = mic
                                isExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier= Modifier.height(50.dp))
            CustomButton(
                "Start Recording and Analyzing",
                listOf(Color.Black, Color.Gray)
                //set the size of the button align with the drop down
            ) {
                Toast.makeText(context, "Analyzing started using $option mic", Toast.LENGTH_LONG).show()
                //start Audio process python function

                //change to display result screen
                val objectList = python.getModule("resultProcess").callAttr("get_emotions_percentage", Gson().toJson(arrayListOf(1,2,3,4,5,6,6,7)))
                initializeFloatList( objectList.asList().map { it.toString().toFloat() } )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRealTimeModeScreen(){
    // Use LocalContext if available, or a default fallback for Preview
    val mockContext = LocalContext.current
    RealTimeModeScreen(mockContext, Modifier.padding(20.dp))
}