package com.temple.aldwairi_projects_emotionecho

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.temple.aldwairi_projects_emotionecho.ui.screens.PracticeModeScreen
import org.junit.Rule
import org.junit.Test

class PracticeModeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test1(){
        composeTestRule.setContent { PracticeModeScreen(
            context = LocalContext.current,
            modifier = Modifier
        ) }

        composeTestRule.onNodeWithText("submit response")
    }
}