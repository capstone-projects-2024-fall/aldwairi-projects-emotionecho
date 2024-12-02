package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoAppRouter
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme
import com.temple.aldwairi_projects_emotionecho.ui.navigation.App
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoAppRouter.currentScreen

import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.LoginScreen

@Composable
fun PrivacySettingScreen(
    context: Context,
    navigateToLogin: () -> Unit = {
        EmotionEchoAppRouter.changeApp(App.Main)
        EmotionEchoAppRouter.navigateTo(LoginScreen)
    }
) {
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
            Text(
                text = "Are you sure you want to log out?",
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )

            CustomButton(
                text = "Log Out",
                onClick = navigateToLogin
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPrivacySettingScreen() {
    AldwairiprojectsemotionechoTheme(darkTheme = false) {
        PrivacySettingScreen(
            context = LocalContext.current,
            navigateToLogin = {}
        )
    }
}
