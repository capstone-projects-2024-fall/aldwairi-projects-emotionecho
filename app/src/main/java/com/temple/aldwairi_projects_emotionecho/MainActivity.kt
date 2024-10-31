package com.temple.aldwairi_projects_emotionecho

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.temple.aldwairi_projects_emotionecho.ui.navigation.App
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoApp
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoAppRouter
import com.temple.aldwairi_projects_emotionecho.ui.screens.PracticeModeScreen
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme
import kotlinx.coroutines.selects.whileSelect

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!Python.isStarted()){
            Python.start(AndroidPlatform(this))
        }

        setContent {
            AldwairiprojectsemotionechoTheme {
                Crossfade(
                    targetState = EmotionEchoAppRouter.currentApp, label = "") { currentApp ->
                    when(currentApp.value){
                        App.Main ->{
                            EmotionEchoApp()
                        }

                        App.Login -> TODO()
                    }
                }
            }
        }
    }
}