package pga.no.sleeper

import platform.windows.*


object WindowsStayAwake {
    const val ES_AWAYMODE_REQUIRED = 0x00000040u
    const val ES_CONTINUOUS = 0x80000000u
    const val ES_DISPLAY_REQUIRED = 0x00000002u
    const val ES_SYSTEM_REQUIRED = 0x00000001u
    const val ES_USER_PRESENT = 0x00000004u

    fun setThreadExecutionState(esFlags: EXECUTION_STATE): EXECUTION_STATE {
        val result = SetThreadExecutionState(esFlags)
        require(result != 0u) {
            "Failed to set thread execution state to: " + toHexString(esFlags)
        }
        return result
    }

    private fun toHexString(i: EXECUTION_STATE): String {
        var state = i
        val builder = StringBuilder()
        while (state != 0u) {
            builder.append(state % 2u)
            state /= 2u
        }
        while (builder.length < 10) builder.append("0")
        return "0x" + builder.reverse().toString()
    }
}