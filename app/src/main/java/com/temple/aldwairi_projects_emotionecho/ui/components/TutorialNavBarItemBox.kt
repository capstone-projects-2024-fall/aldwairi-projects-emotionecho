package com.temple.aldwairi_projects_emotionecho.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TutorialNavBarItemBox(bounds: Rect, stepDescription: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithContent {
                drawContent()
                drawRoundRect(
                    color = Color(0xFFFFFFFF),
                    topLeft = Offset(
                        x = bounds.left,
                        y = bounds.top
                    ),
                    size = Size(bounds.width, bounds.height),
                    blendMode = BlendMode.DstOut,
                    cornerRadius = CornerRadius(50.dp.toPx())
                )
            }
            .background(Color(0xCC000000))
    ){
        val fontSize = 16.sp
        val v = LocalDensity.current
        val textHeight = with(LocalDensity.current){fontSize.toPx()}
        Text(
            text = stepDescription,
            color = Color.White,
            fontSize = fontSize,
            modifier = Modifier.offset {
                IntOffset(
                    x = (bounds.left+16).toInt(),
                    y = (bounds.top - textHeight*20/density).toInt()
                )
            }
                .width(with(v){bounds.width.toDp()})
        )
    }
}