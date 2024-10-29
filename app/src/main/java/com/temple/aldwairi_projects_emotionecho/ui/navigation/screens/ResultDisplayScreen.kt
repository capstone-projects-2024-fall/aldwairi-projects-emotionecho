package com.temple.aldwairi_projects_emotionecho.ui.navigation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class PieChartData(val value: Float, val color: Color, val label: String)
@Composable
fun PieChartWithLegend(data: List<PieChartData>) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            PieChart(data = data)
            Spacer(modifier = Modifier.height(16.dp))
            data.forEach { slice ->
                LegendItem(color = slice.color, text = slice.label)
            }
        }
    }
}

@Composable
fun PieChart(
    data: List<PieChartData>,
    modifier: Modifier = Modifier.size(150.dp),
) {
    val totalValue = data.sumOf { it.value.toDouble() }.toFloat()
    var startAngle = 0f

    Canvas(modifier = modifier.fillMaxSize()) {
        data.forEach { slice ->
            val sweepAngle = (slice.value / totalValue) * 360f
            drawArc(
                color = slice.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
        }
    }
}
@Composable
fun LegendItem(color: Color, text: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(color = color)
        }
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}


@Preview
@Composable
fun PieChartWithLegendExample() {
//    val py = Python.getInstance()
//    val objectList = py.getModule("resultProcess").callAttr("getEmotions", listOf(1,2,3,4,5,6,6,7))
//    val floatList = objectList.asList().map { it.toString().toFloat() }
    val floatList = listOf(10f,20f,20f,10f,10f,10f,20f)
    val data = listOf(
        PieChartData(floatList[0], Color.Gray,"Neutral"),
        PieChartData(floatList[1], Color.Cyan,"Calm"),
        PieChartData(floatList[2], Color.Yellow, "Happy"),
        PieChartData(floatList[3], Color.Blue,"Sad"),
        PieChartData(floatList[4], Color.Red,"Angry"),
        PieChartData(floatList[5], Color(255, 131, 0),"Fearful"),
        PieChartData(floatList[6], Color.Green,"Disgust")
    )
    PieChartWithLegend(data = data)
}