package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment.Vertical
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton
import com.temple.aldwairi_projects_emotionecho.ui.navigation.App
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoAppRouter
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.LoginScreen
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme

@Composable
fun SettingsScreen(
    modifier: Modifier,
    dynamicColor: MutableState<Boolean>
) {
    val showDialog = remember { mutableStateOf(false) }
    var checked by rememberSaveable { mutableStateOf(true) }

    Surface(
        modifier = modifier
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
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Use Custom App Theme")
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            dynamicColor.value = it.not()
                        }
                    )
                }
            }
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
                            EmotionEchoAppRouter.navigateTo(LoginScreen)
                            EmotionEchoAppRouter.changeApp(App.Login)
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
@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun PreviewPrivacySettingScreen() {
    val dc = mutableStateOf(false)
    AldwairiprojectsemotionechoTheme(darkTheme = false) {
        SettingsScreen(Modifier, dc)
    }
}

