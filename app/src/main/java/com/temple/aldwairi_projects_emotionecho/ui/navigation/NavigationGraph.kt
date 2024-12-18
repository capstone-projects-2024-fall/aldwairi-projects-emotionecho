package com.temple.aldwairi_projects_emotionecho.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.temple.aldwairi_projects_emotionecho.DataBaseEE
import com.temple.aldwairi_projects_emotionecho.ui.screens.LogInScreen
import com.temple.aldwairi_projects_emotionecho.ui.screens.PracticeModeScreen
import com.temple.aldwairi_projects_emotionecho.ui.screens.SettingsScreen
import com.temple.aldwairi_projects_emotionecho.ui.screens.RealTimeModeScreen
import com.temple.aldwairi_projects_emotionecho.ui.screens.SignupScreen
import com.temple.aldwairi_projects_emotionecho.userViewModelEE

/**
 * Custom function for creating a NavHost
 *
 * @param modifier Takes in a modifier to adjust for screens using the Scaffold's bottomBar
 * @param navController Used in tandem with the NavHost to switch composable screens
 * @param context Application Context
 * @param dynamicColor Used to give the user a choice between using the custom
 * application color scheme or their system color scheme.
 */
@Composable
fun NavigationGraph(
    modifier: Modifier,
    navController: NavHostController,
    context: Context,
    database: DataBaseEE,
    userViewModelEE: userViewModelEE,
    dynamicColor: MutableState<Boolean>
){
    NavHost(
        navController = navController,
        startDestination = Screen.PracticeModeScreen.screenRoute
    ){
//        TODO: uncomment once all screens are fully implemented
        composable(Screen.LoginScreen.screenRoute) {
            LogInScreen(
                context,
                database = database,
                userViewModelEE = userViewModelEE
            )
        }
        composable(Screen.SingupScreen.screenRoute) {
            SignupScreen(
                context,
                database,
                userViewModelEE
            )
        }
        composable(Screen.SettingsScreen.screenRoute) {
            SettingsScreen(modifier,database,userViewModelEE, dynamicColor)
        }
//        composable(Screen.HomeScreen.screenRoute) {
//            HomeScreen(modifier)
//        }
        composable(Screen.PracticeModeScreen.screenRoute) {
            PracticeModeScreen(context, modifier)
        }
        composable(Screen.RealTimeModeScreen.screenRoute) {
            RealTimeModeScreen(context, modifier)
        }
    }
}