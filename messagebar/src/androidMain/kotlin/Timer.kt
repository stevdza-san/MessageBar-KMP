import java.util.Timer
import kotlin.concurrent.schedule

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class TimerManager {
    private var timer: Timer? = null

    actual fun scheduleTimer(
        visibilityDuration: Long,
        onTimerTriggered: () -> Unit
    ) {
        if (timer != null) {
            // Timer is already in use, cancel it first
            cancelTimer()
        }

        // Create a new Timer instance
        timer = Timer("Message Bar Animation Timer", true)
        timer?.schedule(visibilityDuration) {
            onTimerTriggered()
            // Cancel the timer after triggering the action
            cancelTimer()
        }
    }

    actual fun cancelTimer() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }
}