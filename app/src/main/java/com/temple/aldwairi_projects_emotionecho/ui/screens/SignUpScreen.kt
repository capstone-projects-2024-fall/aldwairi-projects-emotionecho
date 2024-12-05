package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomClickableText
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomDividerWithText
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomTextInput
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.LoginScreen
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme

@Composable
fun SignupScreen(
    context: Context
) {
    val firstNameInput = rememberSaveable { mutableStateOf("") }
    val lastNameInput = rememberSaveable { mutableStateOf("") }
    val usernameInput = rememberSaveable { mutableStateOf("") }
    val passwordInput = rememberSaveable { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            CustomTextInput(
                label = "First Name",
                textInput = firstNameInput,
                imageVector = Icons.Filled.Person,
                testTag = "FIRST_NAME_TEST",
            )
            CustomTextInput(
                label = "Last Name",
                textInput = lastNameInput,
                imageVector = Icons.Filled.Person,
                testTag = "LAST_NAME_TEST",
            )
            CustomTextInput(
                label = "Username",
                textInput = usernameInput,
                imageVector = Icons.Filled.AccountCircle,
                testTag = "USERNAME_TEST",
            )
            CustomTextInput(
                label = "Password",
                textInput = passwordInput,
                imageVector = Icons.Filled.Lock,
                keyboardType = KeyboardType.Password,
                testTag = "PASSWORD_TEST",
            )
            CustomButton(
                text = "Sign Up!"
            ) {
                // Handle sign-up logic here
            }
            CustomDividerWithText()
            CustomClickableText(
                screen = LoginScreen,
                startString = "Already have an account? ",
                aString = "Login here!",
                aStringTag = "NAV",
                aStringAnnotation = "takes you to our login page"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignupScreen(){
    AldwairiprojectsemotionechoTheme(darkTheme = false) {
        SignupScreen(LocalContext.current)
    }
}