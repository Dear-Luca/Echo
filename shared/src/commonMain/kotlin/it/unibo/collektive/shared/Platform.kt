package it.unibo.collektive.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
