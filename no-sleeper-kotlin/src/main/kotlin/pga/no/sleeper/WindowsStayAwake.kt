package pga.no.sleeper

import com.sun.jna.Library
import com.sun.jna.Native

object WindowsStayAwake {
    const val ES_AWAYMODE_REQUIRED = 0x00000040
    const val ES_CONTINUOUS = -0x80000000
    const val ES_DISPLAY_REQUIRED = 0x00000002
    const val ES_SYSTEM_REQUIRED = 0x00000001
    const val ES_USER_PRESENT = 0x00000004

    fun setThreadExecutionState(esFlags: Int): Int {
        val result = Kernel32.INSTANCE.SetThreadExecutionState(esFlags)
        require(result != 0) {
            "Failed to set thread execution state to: " + toHexString(esFlags)
        }
        return result
    }

    private fun toHexString(i: Int): String {
        val builder = StringBuilder("0x")
        builder.append(Integer.toHexString(i))
        while (builder.length < 10) builder.append('0')
        return builder.toString()
    }

    private interface Kernel32 : Library {
        fun SetThreadExecutionState(esFlags: Int): Int

        companion object {
            val INSTANCE: Kernel32 = Native.load(
                "Kernel32",
                Kernel32::class.java, emptyMap<String, Any>()
            )
        }
    }
}