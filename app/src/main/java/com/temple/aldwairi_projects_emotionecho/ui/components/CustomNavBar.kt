package com.temple.aldwairi_projects_emotionecho.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.PracticeModeScreen
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.RealTimeModeScreen

@Composable
fun CustomNavBar(navController: NavController){
    val screen = listOf(PracticeModeScreen, RealTimeModeScreen)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        screen.forEach{ item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.screenRoute)},
                label = { Text(
                    text = item.title,
                    fontSize = 9.sp
                )},
                alwaysShowLabel = true,
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute){
                        item.onClick()

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route){
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomNavBar(){
    val navcon = rememberNavController()
    CustomNavBar(navcon)
}