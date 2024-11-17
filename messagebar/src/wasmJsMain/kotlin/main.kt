package com.stevdza_san.messagebarkmp

import ContentWithMessageBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import rememberMessageBarState

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        val messageBarState = rememberMessageBarState()

        ContentWithMessageBar(
            messageBarState = messageBarState,
            position = MessageBarPosition.BOTTOM
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { messageBarState.addSuccess("Success!") }) {
                    Text(text = "Click me!")
                }
            }
        }
    }
}