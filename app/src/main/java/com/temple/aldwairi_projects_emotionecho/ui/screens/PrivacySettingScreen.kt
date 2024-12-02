package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoAppRouter
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme
import com.temple.aldwairi_projects_emotionecho.ui.navigation.App
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoAppRouter.currentScreen

import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.LoginScreen

@Composable
fun PrivacySettingScreen(
    context: Context
) {
    val showDialog = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            CustomButton(
                text = "Log Out",
                onClick = {
                    showDialog.value = true
                }
            )
        }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showDialog.value = false
                },
                title = {
                    Text(text = "Confirm Logout")
                },
                text = {
                    Text("Are you sure you want to log out?")
                },
                confirmButton = {
                    CustomButton(
                        text = "Yes",
                        onClick = {
                            EmotionEchoAppRouter.changeApp(App.Main)
                            EmotionEchoAppRouter.navigateTo(LoginScreen)
                            showDialog.value = false
                        }
                    )
                },
                dismissButton = {
                    CustomButton(
                        text = "No",
                        onClick = {
                            showDialog.value = false
                        }
                    )
                }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewPrivacySettingScreen() {
    AldwairiprojectsemotionechoTheme(darkTheme = false) {
        PrivacySettingScreen(context = LocalContext.current)
    }
}

