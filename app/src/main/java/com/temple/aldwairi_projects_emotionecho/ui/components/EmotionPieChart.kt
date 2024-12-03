package com.temple.aldwairi_projects_emotionecho.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class PieChartData(val value: Float, val color: Color, val label: String)

fun processFloatList(floatList: List<Float>): List<PieChartData> {
    val data = listOf(
        PieChartData(floatList[0], Color.Gray,"Neutral"),
        PieChartData(floatList[1], Color.Cyan,"Calm"),
        PieChartData(floatList[2], Color.Yellow, "Happy"),
        PieChartData(floatList[3], Color.Blue,"Sad"),
        PieChartData(floatList[4], Color.Red,"Angry"),
        PieChartData(floatList[5], Color(255, 131, 0),"Fearful"),
        PieChartData(floatList[6], Color.Green,"Disgust"),
        PieChartData(floatList[7], Color(0xFF800080),"Surprised")
    )
    return  data
}

@Composable
fun PieChartWithLegend(floatList: List<Float>) {
    val data = processFloatList(floatList)
    val totalValue = data.sumOf { it.value.toDouble() }.toFloat()

    Surface(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Row {
            PieChart(data = data)
            Spacer(modifier = Modifier.width(5.dp))
            Column {
                data.forEach { slice ->
                    val percentage = (slice.value / totalValue) * 100
                    LegendItem(color = slice.color, text = "${slice.label} (${percentage.format(1)}%)")
                }
            }
        }
    }
}

fun Float.format(digits: Int) = "%.${digits}f".format(this)


@Composable
fun PieChart(
    data: List<PieChartData>,
    modifier: Modifier = Modifier
) {
    val totalValue = data.sumOf { it.value.toDouble() }.toFloat()

    // Internal state for previous data values
    val previousData = remember { mutableStateOf(data.map { it.value }) }
    val animatedValues = remember { data.map { Animatable(0f) } }

    // Trigger animations when data changes
    LaunchedEffect(data) {
        data.forEachIndexed { index, slice ->
            val targetValue = (slice.value / totalValue) * 360f
            animatedValues[index].animateTo(targetValue)
        }
        previousData.value = data.map { it.value }
    }

    Canvas(modifier = modifier.size(150.dp)) {
        var startAngle = 0f
        data.forEachIndexed { index, slice ->
            val sweepAngle = animatedValues[index].value
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
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(color = color)
        }
        Spacer(Modifier.width(5.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}


@Preview
@Composable
fun PieChartWithLegendExample() {
//    val py = Python.getInstance()
//    val objectList = py.getModule("resultProcess").callAttr("getEmotions", listOf(1,2,3,4,5,6,6,7))
//    val floatList = objectList.asList().map { it.toString().toFloat() }
    val floatList = listOf(10f,20f,20f,10f,10f,10f,10f,10f)
    PieChartWithLegend(floatList)
}