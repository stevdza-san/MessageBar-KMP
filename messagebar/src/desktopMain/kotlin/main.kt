import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MessageBar KMP",
    ) {
        val messageBarState = rememberMessageBarState()
        ContentWithMessageBar(messageBarState = messageBarState) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {messageBarState.addSuccess("Success!")}) {
                    Text(text = "Success")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {messageBarState.addError(Exception("Internet not available."))}) {
                    Text(text = "Error")
                }
            }
        }
    }
}