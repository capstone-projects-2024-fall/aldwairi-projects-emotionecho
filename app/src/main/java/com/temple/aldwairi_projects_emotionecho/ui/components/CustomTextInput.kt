package com.temple.aldwairi_projects_emotionecho.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview


/**
 * Creates a custom TextInput
 * @param label name of the text input type
 * @param textInput a mutable String used to retain input through state changes
 * @param imageVector displays a leading image in the TextInput
 * @param keyboardType used to change depending on normal text input vs password input, etc.
 * @param testTag String used in test classes
 * @throws IllegalArgumentException when these conditions are not met simultaneously:
 * - imageVector = Icons.Filled.Lock
 * - keyboardType = keyboardType.Password
 *
 * Both of these must be true in order to create a Password TextInput
 */
@Composable
fun CustomTextInput(
    label: String,
    textInput: MutableState<String>,
    imageVector: ImageVector = Icons.Filled.Android,
    keyboardType: KeyboardType = KeyboardType.Text,
    testTag: String
){
    if ((imageVector == Icons.Filled.Lock && keyboardType != KeyboardType.Password) ||
        (keyboardType == KeyboardType.Password && imageVector != Icons.Filled.Lock)){
        throw IllegalArgumentException(
            "the ImageVector Icons.Filled.Lock must be matched with the KeyboardType.Password"
        )
    }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        value = textInput.value,
        onValueChange = { textInput.value = it },
        leadingIcon = { Icon(imageVector, contentDescription = null) },
        visualTransformation = if (imageVector == Icons.Filled.Lock && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = if (imageVector == Icons.Filled.Lock) {
            {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            }
        } else null  // No trailing icon if the icon is not a lock
    )
}

@Preview
@Composable
fun previewTextInput(){
    val textInput = remember { mutableStateOf("") }
    CustomTextInput(label = "Password",
        textInput = textInput,
        testTag = "preview",
        imageVector = Icons.Filled.Lock)
}