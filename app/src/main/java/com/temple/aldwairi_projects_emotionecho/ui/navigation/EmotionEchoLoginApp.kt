package com.temple.aldwairi_projects_emotionecho.ui.navigation

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.temple.aldwairi_projects_emotionecho.ui.screens.LogInScreen
import com.temple.aldwairi_projects_emotionecho.ui.screens.SignupScreen

@Composable
fun EmotionEchoLoginApp(context: Context){
    Surface (
        modifier = Modifier.fillMaxWidth()
    ){
        Crossfade(
            targetState = EmotionEchoAppRouter.currentScreen,
            label = ""
        ) { currentState ->
            when(currentState.value){
                is Screen.LoginScreen -> LogInScreen(context)
                is Screen.SingupScreen -> SignupScreen(context)
                else -> {}
            }
        }
    }
}