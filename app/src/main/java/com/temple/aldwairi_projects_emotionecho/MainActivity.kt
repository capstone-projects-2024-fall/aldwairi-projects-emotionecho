package com.temple.aldwairi_projects_emotionecho

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.temple.aldwairi_projects_emotionecho.ui.navigation.screens.PracticeModeScreen
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!Python.isStarted()){
            Python.start(AndroidPlatform(this))
        }

        setContent {
            AldwairiprojectsemotionechoTheme {
                PracticeModeScreen(this)
            }
        }
    }
}