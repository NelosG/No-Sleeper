package pga.no.sleeper

import java.io.File
import kotlin.jvm.optionals.getOrNull

class MainExe {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val killFile = File("kill-${ProcessHandle.current().pid()}.bat")
            killFile.printWriter().use { out ->
                val pid = ProcessHandle.current().parent().getOrNull()?.pid()
                if (pid != null) {
                    out.print("TASKKILL /PID $pid /F")
                } else {
                    out.print("TASKKILL /IM no-sleeper-java.exe /F")
                }
            }
            killFile.deleteOnExit()

            StayAwakeService.start(args.getOrNull(0) == "display")
            ProcessHandle.current().parent().getOrNull()?.onExit()?.thenRun {
                println("STOPPED")
                StayAwakeService.stop()
            }
            StayAwakeService.await()
        }
    }
}