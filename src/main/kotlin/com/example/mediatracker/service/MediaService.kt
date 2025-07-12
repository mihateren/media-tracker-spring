package com.example.mediatracker.service

import com.example.mediatracker.api.dto.media.ChangeMediaStateRequest
import com.example.mediatracker.common.auth.AuthUserDetails
import com.example.mediatracker.common.exception.entity.KinopoiskException
import com.example.mediatracker.external.KinopoiskClient
import com.example.mediatracker.external.dto.MediaDetailsResponse
import com.example.mediatracker.external.dto.SearchResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class MediaService(
    val kinopoiskClient: KinopoiskClient
) {

    fun searchMedia(keyword: String): SearchResponse? {
        val response = kinopoiskClient.searchMedia(keyword)
        return when (response.statusCode) {
            response.statusCode -> response.body
            response.statusCode -> null
            else -> throw KinopoiskException("Error occurred while searching $keyword")
        }
    }

    fun getMediaDetailsById(id: Int): MediaDetailsResponse? {
        val response = kinopoiskClient.getMediaDetailsById(id)
        return when (response.statusCode) {
            response.statusCode -> response.body
            response.statusCode -> null
            else -> throw KinopoiskException("Unexpected status: ${response.statusCode}")
        }
    }
}