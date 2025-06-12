package com.example.mediatracker.external.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SearchResponse(
    val keyword: String,
    val pagesCount: Int,
    val films: List<Film>
) {
    /** Информация о фильме */
    data class Film(
        val filmId: Long,
        val nameRu: String?,
        val nameEn: String?,
        val type: String,
        val year: String?,
        val description: String?,
        val filmLength: String?,
        val countries: List<Country>,
        val genres: List<Genre>,
        val rating: String?,
        val ratingVoteCount: Int,
        val posterUrl: String?,
        val posterUrlPreview: String?
    ) {
        data class Country(
            @JsonProperty("country")
            val name: String
        )

        data class Genre(
            @JsonProperty("genre")
            val name: String
        )
    }
}
