package com.example.mediatracker.api.dto

class MediaDto(
    val title: String,
    val type: String,
    val description: String,
    val coverUrl: String,
    val country: String,
    val year: Int,
) {
    class kinopoisk(
        val rating: Double,
    ) {

    }
}