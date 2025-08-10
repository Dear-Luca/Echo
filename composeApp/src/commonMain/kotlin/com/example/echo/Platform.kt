package com.example.echo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform