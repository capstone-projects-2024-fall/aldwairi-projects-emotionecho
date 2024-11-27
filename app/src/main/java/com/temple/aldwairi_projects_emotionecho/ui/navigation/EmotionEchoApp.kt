package com.temple.aldwairi_projects_emotionecho.ui.navigation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomNavBar
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme

@Composable
fun EmotionEchoApp(context: Context){
    var isTutorialActive by rememberSaveable { mutableStateOf(true) }
    var tutorialStep by rememberSaveable { mutableIntStateOf(0) }
    val navBarItemLocationOnScreen = remember {mutableStateOf<Map<String, LayoutCoordinates>>(emptyMap())}

    AldwairiprojectsemotionechoTheme{
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = { CustomNavBar(navController, navBarItemLocationOnScreen) }
            ) { innerPadding ->

                NavigationGraph(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    context = context
                )
            }

            if(isTutorialActive){
                TutorialOverlay(navBarItemLocationOnScreen.value)
            }
        }
    }
}

@Composable
fun TutorialOverlay(navBarItemLocation: Map<String, LayoutCoordinates>) {
    val targetLocation = navBarItemLocation[Screen.PracticeModeScreen.screenRoute]?.boundsInRoot()
    val density = LocalContext.current.resources.displayMetrics.density
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x88000000)) // semi-transparent overlay
    ){
        targetLocation?.let { bounds ->
            Box(
                modifier = Modifier
                    .offset(
                        x = (bounds.left/density).dp,
                        y = (bounds.top/density).dp
                    )
                    .size((bounds.width/density).dp,(bounds.height/density).dp) // Replace with proper highlight size
                    .background(
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f) ,
                        shape = RoundedCornerShape(50.dp)) // Highlight example
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEmotionEchoApp(){
    EmotionEchoApp(LocalContext.current)
}