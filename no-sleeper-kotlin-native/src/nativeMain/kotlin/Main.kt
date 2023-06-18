import kotlinx.cinterop.memScoped
import kotlinx.cinterop.staticCFunction
import pga.no.sleeper.StayAwakeService
import platform.posix.*
import platform.windows.CreateMutexA
import platform.windows.ERROR_ALREADY_EXISTS
import platform.windows.GetCurrentProcessId
import platform.windows.GetLastError
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val mutexName = "Local\\no-sleeper-kotlin"
    CreateMutexA(null, 0, mutexName);
    if (GetLastError().toInt() == ERROR_ALREADY_EXISTS) {
        exitProcess(-1)
    }

    val pid = GetCurrentProcessId()
    val fileName = "kill-$pid.bat"
    val killFile = fopen(fileName, "w") ?: error("File not opened")

    val text = """
        TASKKILL /PID $pid /F
        DEL $fileName
    """.trimIndent()

    memScoped {
        if (fputs(text, killFile) == EOF) throw Error("File write error")
    }
    fclose(killFile)

    val terminationHandler = staticCFunction<Int, Unit> {
        remove("kill-${GetCurrentProcessId()}.bat")
        exitProcess(0)
    }
    signal(SIGINT, terminationHandler);
    signal(SIGTERM, terminationHandler);
    StayAwakeService.start(args.getOrNull(0) == "display")
    StayAwakeService.await()
    StayAwakeService.stop()
}