package com.temple.aldwairi_projects_emotionecho.ui.navigation

import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(var title: String, var icon: ImageVector, var screenRoute: String, val showBottomBar: Boolean, var onClick: () -> Unit){
    object LoginScreen: Screen("Login", Default.Person, "login", false, {})
    object RegisterSCreen: Screen("Register", Default.Person, "register", false, {})
    object HomeScreen: Screen("Home", Default.Home, "home", true, {})
    object PracticeModeScreen: Screen("PracticeMode", Default.Psychology, "practice", true, {})
    object RealTimeModeScreen: Screen("RealTimeMode", Default.Hearing, "real_time", true, {})
}

sealed class App{
    object Login: App()
    object Main: App()
}

object EmotionEchoAppRouter {
//    TODO: change default App to Login once fully implemented
    val currentApp:MutableState<App> = mutableStateOf(App.Main)

//     TODO: change default Screen to LoginScreen once fully implemented
    val currentScreen: MutableState<Screen> = mutableStateOf(Screen.PracticeModeScreen)

    fun navigateTo(destinationScreen: Screen){
        currentScreen.value = destinationScreen
    }
    fun changeApp(app: App){
        currentApp.value = app
    }
}