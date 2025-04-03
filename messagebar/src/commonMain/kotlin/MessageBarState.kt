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
        clearErrors()
        success = message
        updated = !updated
    }

    fun addError(message: String) {
        clearErrors()
        errorMessage = message
        updated = !updated
    }

    fun addError(exception: Exception) {
        clearErrors()
        errorException = exception
        updated = !updated
    }

    internal fun reset() {
        success = null
        clearErrors()
        updated = !updated
    }

    private fun clearErrors() {
        errorMessage = null
        errorException = null
    }
}