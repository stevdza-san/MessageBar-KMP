import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Model class used to hold success/error information, as well as trigger
 * success/error Message Bar (addSuccess/addError).
 * */
class MessageBarState {
    var success by mutableStateOf<String?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var errorException by mutableStateOf<Exception?>(null)
        private set
    internal var updated by mutableStateOf(false)
        private set

    fun addSuccess(message: String) {
        clearMessages()
        success = message
        updated = !updated
    }

    fun addError(message: String) {
        clearMessages()
        errorMessage = message
        updated = !updated
    }

    fun addError(exception: Exception) {
        clearMessages()
        errorException = exception
        updated = !updated
    }

    internal fun reset() {
        clearMessages()
        updated = !updated
    }

    private fun clearMessages() {
        success = null
        errorMessage = null
        errorException = null
    }
}