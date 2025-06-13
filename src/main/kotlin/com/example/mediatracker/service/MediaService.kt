package com.example.mediatracker.service

import com.example.mediatracker.external.KinopoiskClient
import com.example.mediatracker.external.dto.SearchResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class MediaService(
    val kinopoiskClient: KinopoiskClient
) {

    private val logger = KotlinLogging.logger {}
    fun searchMedia(keyword: String): SearchResponse {
        return kinopoiskClient.searchMedia(keyword)
    }

}