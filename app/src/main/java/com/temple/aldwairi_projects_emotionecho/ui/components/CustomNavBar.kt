package com.temple.aldwairi_projects_emotionecho.ui.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.PracticeModeScreen
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.RealTimeModeScreen
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme

@Composable
fun CustomNavBar(navController: NavController, navItemCoordinates: MutableState<Map<String, LayoutCoordinates>>){
    val screen = listOf(PracticeModeScreen, RealTimeModeScreen)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        screen.forEach{ item ->
            NavigationBarItem(
                icon = { Icon(item.icon,
                    contentDescription = item.screenRoute,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer)},
                label = { Text(
                    text = item.title,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
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
                },
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    navItemCoordinates.value = navItemCoordinates.value.toMutableMap().apply {
                        put(item.screenRoute, coordinates)
                    }
                }
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun PreviewCustomNavBar(){
    val lc = mutableStateOf<Map<String, LayoutCoordinates>>(emptyMap())
    AldwairiprojectsemotionechoTheme(darkTheme = true) {
        val navcon = rememberNavController()
        CustomNavBar(navcon, lc)
    }
}