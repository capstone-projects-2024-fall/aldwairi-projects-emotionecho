package com.temple.aldwairi_projects_emotionecho.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomNavBar
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme

@Composable
fun EmotionEchoApp(){
    AldwairiprojectsemotionechoTheme{
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = { CustomNavBar(navController) }
            ) { innerPadding ->
                NavigationGraph(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController
                )
            }
        }
    }
}