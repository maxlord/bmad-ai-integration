package com.maxlord.bmadandroiddemo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class HelloWorldScreenTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun tappingButtonShowsHelloWorld() {
        composeRule.setContent { HelloWorldScreen() }

        composeRule.onNodeWithText("Tap the button").assertIsDisplayed()
        composeRule.onNodeWithText("Say hello").performClick()
        composeRule.onNodeWithText("Hello World").assertIsDisplayed()
    }
}
