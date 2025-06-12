package com.example.mediatracker.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

interface Logging {
    val log: KLogger get() = KotlinLogging.logger(this::class.java.name)
}