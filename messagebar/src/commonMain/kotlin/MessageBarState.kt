import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Model class used to hold success/error information (success/error), as well as trigger
 * success/error Message Bar (addSuccess/addError).
 * */
class MessageBarState {
    var success by mutableStateOf<String?>(null)
        private set
    var error by mutableStateOf<Exception?>(null)
        private set
    internal var updated by mutableStateOf(false)
        private set

    fun addSuccess(message: String) {
        error = null
        success = message
        updated = !updated
    }

    fun addError(exception: Exception) {
        success = null
        error = exception
        updated = !updated
    }

    internal fun reset() {
        success = null
        error = null
        updated = !updated
    }
}