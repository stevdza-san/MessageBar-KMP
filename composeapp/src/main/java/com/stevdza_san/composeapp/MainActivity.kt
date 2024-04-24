package com.stevdza_san.composeapp

import ContentWithMessageBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.stevdza_san.composeapp.ui.theme.MessageBarKMPTheme
import rememberMessageBarState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageBarKMPTheme {
                val messageBarState = rememberMessageBarState()

                ContentWithMessageBar(
                    messageBarState = messageBarState
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
    }
}