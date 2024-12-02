package com.temple.aldwairi_projects_emotionecho.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TutorialNavBarItemBox(bounds: Rect, stepDescription: String, focusNavBarItem: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit){
                detectTapGestures {  }
            }
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithContent {
                drawContent()
                if(focusNavBarItem){
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
            }
            .background(Color(0xCC000000))
    ){
        val fontSize = 16.sp
        val screenDensity = LocalDensity.current
        val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
        val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels

        val textHeight = with(screenDensity){fontSize.toPx()}
        val textWidth = with(screenDensity){(bounds.width - 16).toSp().toPx()}

        val estimatedTextLines = (stepDescription.length/(textWidth/fontSize.value)).toInt() + 1
        val totalTextHeight = estimatedTextLines * textHeight * 3
        Text(
            text = stepDescription,
            color = Color.White,
            fontSize = fontSize,
            modifier = Modifier.offset {
                if(focusNavBarItem){
                    Log.d("TUTORIAL", "x = ${bounds.left}, y = ${bounds.right}")
                    val offsetY = bounds.top - totalTextHeight
                    IntOffset(
                        x = (bounds.left+16).toInt(),
                        y = if (offsetY < 0) 0 else offsetY.toInt()
                    )
                }else{
                    Log.d("TUTORIAL", "x = ${(screenWidth / 2) - (bounds.width / 2)}, y = ${(screenHeight / 4)}")
                    IntOffset(
                        x = (screenWidth / 2) - (bounds.width / 2).toInt(),
                        y = (screenHeight / 4) // Position closer to the top
                    )
                }
            }
                .width(with(screenDensity){bounds.width.toDp()-16.dp})
        )
    }
}