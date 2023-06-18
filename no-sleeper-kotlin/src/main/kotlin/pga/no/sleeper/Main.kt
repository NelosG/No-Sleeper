package pga.no.sleeper

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            StayAwakeService.start(args.getOrNull(0) == "display")
            StayAwakeService.await()
            StayAwakeService.stop()
        }
    }
}