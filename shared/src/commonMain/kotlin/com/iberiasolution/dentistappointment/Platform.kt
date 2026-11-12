package com.iberiasolution.dentistappointment

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect fun currentTimeMillis(): Long