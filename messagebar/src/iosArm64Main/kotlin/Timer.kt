import platform.Foundation.NSTimer

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class TimerManager {
    private var timer: NSTimer? = null

    actual fun scheduleTimer(
        visibilityDuration: Long,
        onTimerTriggered: () -> Unit
    ) {
        timer = NSTimer.scheduledTimerWithTimeInterval(
            visibilityDuration.toDouble() / 1000,
            repeats = false,
            block = { onTimerTriggered() }
        )
    }

    actual fun cancelTimer() {
        timer?.invalidate()
        timer = null
    }
}