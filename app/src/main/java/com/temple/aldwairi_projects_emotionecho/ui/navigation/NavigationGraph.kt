package com.temple.aldwairi_projects_emotionecho.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.temple.aldwairi_projects_emotionecho.ui.screens.PracticeModeScreen
import com.temple.aldwairi_projects_emotionecho.ui.screens.RealTimeModeScreen

@Composable
fun NavigationGraph(
    modifier: Modifier,
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.PracticeModeScreen.screenRoute){
//        TODO: uncomment once all screens are fully implemented
//        composable(Screen.LoginScreen.screenRoute) {
//            LoginScreen()
//        }
//        composable(Screen.RegisterScreen.screenRoute) {
//            RegisterScreen()
//        }
//        composable(Screen.SettingsScreen.screenRoute) {
//            SettingsScreen(modifier)
//        }
//        composable(Screen.HomeScreen.screenRoute) {
//            HomeScreen(modifier)
//        }
        composable(Screen.PracticeModeScreen.screenRoute) {
            PracticeModeScreen(LocalContext.current, modifier)
        }
        composable(Screen.RealTimeModeScreen.screenRoute) {
            RealTimeModeScreen(LocalContext.current, modifier)
        }
    }
}