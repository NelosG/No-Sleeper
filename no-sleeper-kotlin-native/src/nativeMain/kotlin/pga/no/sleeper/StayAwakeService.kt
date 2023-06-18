package pga.no.sleeper

import kotlinx.coroutines.*

@ThreadLocal
object StayAwakeService {
    private lateinit var job: Job
    private var isRunning = false

    fun start(display: Boolean = false) {
        if (display) println("DISPLAY")
        if (isRunning) return
        var option = WindowsStayAwake.ES_SYSTEM_REQUIRED or WindowsStayAwake.ES_CONTINUOUS
        if (display) {
            option = option or WindowsStayAwake.ES_DISPLAY_REQUIRED
        }
        isRunning = true

        WindowsStayAwake.setThreadExecutionState(option)
        job = Job()
    }

    fun await() {
        runBlocking {
            job.join()
            println("JOB JOINED $isRunning")
        }
    }

    fun stop() {
        println("STOPPING")
        isRunning = false
        job.cancel()
    }
}
