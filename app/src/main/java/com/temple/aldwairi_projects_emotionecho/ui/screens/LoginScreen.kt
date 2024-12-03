package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.temple.aldwairi_projects_emotionecho.DataBaseEE
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomClickableText
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomDividerWithText
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomTextInput
import com.temple.aldwairi_projects_emotionecho.ui.navigation.App
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoAppRouter
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.RealTimeModeScreen
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.SingupScreen
import com.temple.aldwairi_projects_emotionecho.userViewModelEE

@Composable
fun LogInScreen(
    context: Context,
    database : DataBaseEE,
    userViewModelEE : userViewModelEE
){
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
                testTag = "USERNAME_TEST",
            )
            CustomButton(
                text = "Login",
            ) { database.signIn(usernameInput.value ,passwordInput.value,{database.getUserByID(
                it.uid,
                { _, user ->
                    userViewModelEE.setCurrentUser(user!!)

                },
                {
                    Log.d("Login", "Error Getting User")
                }
            )

                EmotionEchoAppRouter.changeApp(App.Main)
                EmotionEchoAppRouter.navigateTo(Screen.PracticeModeScreen)},{Log.d("Login", it.toString())
                if(it is FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(
                        context,
                        "Email or password is incorrect",
                        Toast.LENGTH_SHORT
                    ).show()
                } else{
                    Toast.makeText(
                        context,
                        "There was an error signing in try again soon.",
                        Toast.LENGTH_SHORT
                    ).show()
                }}) }
            DividerDefaults.apply {
                color.blue
                Thickness.div(1.dp)
            }
            CustomDividerWithText()
            CustomClickableText(
                screen = SingupScreen,
                startString = "Don't have an account? ",
                aString = "Signup here!",
                aStringTag = "NAV",
                aStringAnnotation = "takes you to our signup page"
            )
            CustomDividerWithText()
            CustomClickableText(
                screen = RealTimeModeScreen,
                startString = "Enter as a ",
                aString = "Guest",
                aStringTag = "NAV",
                aStringAnnotation = "takes you to our signup page"
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewLogInScreen(){
//    AldwairiprojectsemotionechoTheme(darkTheme = false) {
//        LogInScreen(LocalContext.current)
//    }
//}