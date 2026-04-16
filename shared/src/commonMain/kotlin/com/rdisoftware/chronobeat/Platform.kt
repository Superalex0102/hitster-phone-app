package com.rdisoftware.chronobeat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform