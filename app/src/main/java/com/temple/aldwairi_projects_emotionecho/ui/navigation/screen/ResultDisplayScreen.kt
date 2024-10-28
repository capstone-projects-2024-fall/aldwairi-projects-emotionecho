package com.temple.aldwairi_projects_emotionecho.ui.navigation.screen

import android.graphics.Paint.Align
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

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
    val data = listOf(
        PieChartData(20f, Color.Blue,"Neutral"),
        PieChartData(20f, Color.Red,"Calm"),
        PieChartData(20f, Color.Green, "Happy"),
        PieChartData(10f, Color.Blue,"Sad"),
        PieChartData(10f, Color.Blue,"Angry"),
        PieChartData(10f, Color.Blue,"Fearful"),
        PieChartData(10f, Color.Blue,"Disgust")
    )
    PieChartWithLegend(data = data)
}