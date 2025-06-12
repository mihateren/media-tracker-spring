package com.example.mediatracker.service

import com.example.mediatracker.external.KinopoiskClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class MediaService(
    val kinopoiskClient: KinopoiskClient
) {

    private val logger = KotlinLogging.logger {}
    fun searchMedia(keyword: String): List<String> {
        return kinopoiskClient.searchMedia(keyword)
    }

}