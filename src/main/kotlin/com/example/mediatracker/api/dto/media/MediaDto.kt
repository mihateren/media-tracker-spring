package com.example.mediatracker.api.dto.media

import com.example.mediatracker.external.dto.MediaDetailsResponse

data class MediaDto(
    val id: Long,
    val kinopoiskId: Int,
    val type: String,
    val title: String,
    val details: MediaDetailsResponse
)