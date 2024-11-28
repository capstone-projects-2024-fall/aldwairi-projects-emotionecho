package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton
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
