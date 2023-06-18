package pga.no.sleeper

import pga.no.sleeper.WindowsStayAwake.setThreadExecutionState
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException


object StayAwakeService {
    private lateinit var scheduledFuture: CompletableFuture<Unit>
    private var isRunning = false

    fun start(display: Boolean = false) {
        if(display) println("DISPLAY")
        if (isRunning) return
        var option = WindowsStayAwake.ES_SYSTEM_REQUIRED or WindowsStayAwake.ES_CONTINUOUS
        if (display) {
            option = option or WindowsStayAwake.ES_DISPLAY_REQUIRED
        }
        setThreadExecutionState(option)
        isRunning = true
        scheduledFuture = CompletableFuture()
    }

    fun await() {
        try {
            scheduledFuture.join()
        } catch (_: CompletionException) {

        } catch (_: CancellationException) {

        }
    }

    fun stop() {
        scheduledFuture.complete(Unit)
        isRunning = false
    }
}
