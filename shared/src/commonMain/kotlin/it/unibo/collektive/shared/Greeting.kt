package it.unibo.collektive.shared

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Echo, ${platform.name}!"
    }
}
