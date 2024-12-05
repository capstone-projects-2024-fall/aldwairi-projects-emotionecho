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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.temple.aldwairi_projects_emotionecho.DataBaseEE
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomClickableText
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomDividerWithText
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomTextInput
import com.temple.aldwairi_projects_emotionecho.ui.navigation.App
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoAppRouter
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen.LoginScreen
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme
import com.temple.aldwairi_projects_emotionecho.userViewModelEE

@Composable
fun SignupScreen(
    context: Context,
    dataBaseEE: DataBaseEE,
    userViewModelEE: userViewModelEE
){
    val usernameInput = rememberSaveable { mutableStateOf("") }
    val passwordInput = rememberSaveable { mutableStateOf("") }
    val firstNameInput = rememberSaveable { mutableStateOf("") }
    val lastNameInput = rememberSaveable { mutableStateOf("") }

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
                testTag = "USERNAME_TEST",
            )
            CustomTextInput(
                label = "Last Name",
                textInput = lastNameInput,
                imageVector = Icons.Filled.Person,
                testTag = "USERNAME_TEST",
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
                testTag = "USERNAME_TEST",
            )
            CustomButton(
                text = "Sign Up!"
            ) {
                if(checkInputFields(
                    context,
                    firstNameInput.value,
                    lastNameInput.value,
                    passwordInput.value,
                    usernameInput.value
                )){
                    dataBaseEE.createUser(
                        firstName = firstNameInput.value,
                        lastName = lastNameInput.value,
                        email = usernameInput.value,
                        password = passwordInput.value,
                        { firebaseUser, user ->
                            userViewModelEE.setCurrentUser(user)
                            EmotionEchoAppRouter.changeApp(App.Main)
                            EmotionEchoAppRouter.navigateTo(Screen.PracticeModeScreen)
                        },
                        {
                            Log.d("Register Error",it.toString())
                            if(it is FirebaseAuthUserCollisionException){
                                Toast.makeText(
                                    context,
                                    "Email address already in use",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else{
                                Toast.makeText(
                                    context,
                                    "There was an error creating your account try again soon.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    )
                }
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

/**
 * Checks all fields provided to see if they are valid using [checkIfFieldIsValid]
 * @param firstNameField - The string element from the first name text edit field
 * @param lastNameField - The string element from the last name text edit field
 * @param emailField - The string element from the email text edit field
 * @param passwordField - The string element from the password text edit field
 * @return Boolean
 *
 */
private fun checkInputFields(
    context: Context,
    firstNameField: String,
    lastNameField: String,
    emailField: String,
    passwordField: String
): Boolean{
    return checkIfFieldIsValid(
        context,
        firstNameField,
        "Please enter a value for First Name"
    ) &&
            checkIfFieldIsValid(
                context,
                lastNameField,
                "Please enter a value for Last Name"
            ) &&
            checkIfFieldIsValid(
                context,
                emailField,
                "Please enter a value for Email"
            ) &&
            checkIfFieldIsValid(
                context,
                passwordField,
                "Please enter a value for Password"
            )
}


/**
 * Checks if the given text field is blank
 * and if not it displays a toast with the given error message
 * @param context - Application context used to display the toast
 * @param field - The string field that is to be checked
 * @param errorMessage - A error message that describes the problem to the user
 * @return Boolean
 */
private fun checkIfFieldIsValid(
    context: Context,
    field: String,
    errorMessage: String
): Boolean{
    if(field.isNotBlank()){
        return true
    }
    Toast.makeText(
        context,
        errorMessage,
        Toast.LENGTH_SHORT
    ).show()
    return false
}





//@Preview(showBackground = true)
//@Composable
//fun PreviewSignupScreen(){
//    AldwairiprojectsemotionechoTheme(darkTheme = false) {
//        SignupScreen(LocalContext.current)
//    }
//}