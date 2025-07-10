package com.example.mediatracker.service

import com.example.jooq.generated.tables.pojos.Genres
import com.example.mediatracker.common.logging.Logging
import com.example.mediatracker.external.dto.MediaDetailsResponse
import com.example.mediatracker.repository.GenreRepository
import org.springframework.stereotype.Service

@Service
class GenreService(
    val genreRepository: GenreRepository,
): Logging {

    fun addGenresFromMedia(media: MediaDetailsResponse, mediaId: Long) {
        media.genres
            .mapNotNull { it.genre }
            .filter { it.isNotBlank() }
            .distinct()
            .forEach { genreName ->
                try {
                    val genre = genreRepository.findByTitle(genreName)
                        ?: genreRepository.save(Genres(title = genreName))

                    if (genre?.id != null) {
                        genreRepository.saveMediaGenres(mediaId, genre.id!!)
                    }
                } catch (e: Exception) {
                    log.error(e) { "Ошибка добавления жанра '$genreName' для медиа $mediaId" }
                }
            }
    }
}