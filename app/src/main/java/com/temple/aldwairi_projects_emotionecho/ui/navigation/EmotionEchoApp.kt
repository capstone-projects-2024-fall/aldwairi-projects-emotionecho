package com.temple.aldwairi_projects_emotionecho.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomNavBar
import com.temple.aldwairi_projects_emotionecho.ui.components.TutorialNavBarItemBox
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.PracticeModeScreen
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.RealTimeModeScreen
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.SettingsScreen
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme
import com.temple.aldwairi_projects_emotionecho.R.string.tutorial_step_Introduction_description
import com.temple.aldwairi_projects_emotionecho.R.string.tutorial_step_PracticeMode_description
import com.temple.aldwairi_projects_emotionecho.R.string.tutorial_step_RealTimeMode_description
import com.temple.aldwairi_projects_emotionecho.R.string.tutorial_step_SettingsScreen_description

@Composable
fun EmotionEchoApp(context: Context){
    var isTutorialActive by rememberSaveable { mutableStateOf(true) }
    var tutorialStep by rememberSaveable { mutableIntStateOf(0) }
    val navBarItemLocationOnScreen = remember {mutableStateOf<Map<String, LayoutCoordinates>>(emptyMap())}
    val dynamicColor = remember { mutableStateOf(false) }

    AldwairiprojectsemotionechoTheme(
        dynamicColor = dynamicColor.value
    ){
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
                    context = context,
                    dynamicColor
                )
            }

            if (isTutorialActive) {
                TutorialOverlay(navBarItemLocationOnScreen.value, tutorialStep)

                // Step navigation buttons (for example)
                Box(modifier = Modifier.fillMaxSize()) {
                    // Add buttons for "Next" and "Previous"
                    Button(
                        onClick = { if (tutorialStep < navBarItemLocationOnScreen.value.size) tutorialStep++ },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text("Next")
                    }
                    Button(
                        onClick = { if (tutorialStep > 0) tutorialStep-- },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Text("Previous")
                    }
                    Button(
                        onClick = { isTutorialActive = false },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text("Exit Tutorial")
                    }
                }
            }
        }
    }
}

data class TutorialStep(val screenRoute: String?, val description: String)

@Composable
fun TutorialOverlay(navBarItemLocation: Map<String, LayoutCoordinates>, tutorialStep: Int) {
    val tutorialSteps = listOf(
        TutorialStep(null, stringResource(tutorial_step_Introduction_description)),
        TutorialStep(PracticeModeScreen.screenRoute, stringResource(tutorial_step_PracticeMode_description)),
        TutorialStep(RealTimeModeScreen.screenRoute, stringResource(tutorial_step_RealTimeMode_description)),
        TutorialStep(SettingsScreen.screenRoute, stringResource(tutorial_step_SettingsScreen_description))
    )

    if(tutorialStep in tutorialSteps.indices){
        val step = tutorialSteps[tutorialStep].screenRoute
        if (step != null){
            navBarItemLocation[tutorialSteps[tutorialStep].screenRoute]
                ?.boundsInRoot()
                ?.let { bounds ->
                    TutorialNavBarItemBox(bounds, tutorialSteps[tutorialStep].description, true)
                }
        }else{
            val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels.toFloat()
            val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels.toFloat()
            val r = Rect(
                Offset(screenWidth * 0.1f, screenHeight * 0.1f),
                Size(screenWidth * 0.8f, screenHeight) // Size to fit text comfortably
            )

            TutorialNavBarItemBox(r, tutorialSteps[tutorialStep].description, false)
        }
    }

//    val targetLocation = navBarItemLocation[RealTimeModeScreen.screenRoute]?.boundsInRoot()
//    targetLocation?.let {bounds ->
//        TutorialNavBarItemBox(bounds, tutorialSteps[1].description, true)
//    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEmotionEchoApp(){
    EmotionEchoApp(LocalContext.current)
}