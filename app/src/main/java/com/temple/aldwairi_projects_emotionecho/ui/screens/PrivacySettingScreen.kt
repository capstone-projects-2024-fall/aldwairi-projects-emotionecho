package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton
import com.temple.aldwairi_projects_emotionecho.ui.theme.TempleCherryRed
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoAppRouter
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen


@Composable
fun PrivacySettingScreen(
    context: Context,
    //navigateToLogin: () -> Unit
    navigateToLogin: () -> Unit = {EmotionEchoAppRouter.navigateTo(Screen.LoginScreen) }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Are you sure you want to log out?")

        Spacer(modifier = Modifier.height(20.dp))

        CustomButton(
            text = "Log Out",
            brush = listOf(TempleCherryRed, Black), // You can customize the colors if desired
            onClick = navigateToLogin
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogoutScreen() {
    val mockNavigateToLogin = {}
    PrivacySettingScreen(LocalContext.current, navigateToLogin = mockNavigateToLogin)
}
