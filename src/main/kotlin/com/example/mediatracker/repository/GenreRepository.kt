package com.example.mediatracker.repository

import com.example.jooq.generated.tables.pojos.Genres
import com.example.jooq.generated.tables.pojos.MediaGenres
import com.example.jooq.generated.tables.references.GENRES
import com.example.jooq.generated.tables.references.MEDIA_GENRES
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class GenreRepository(
    private val dsl: DSLContext
) {

    fun findByTitle(title: String): Genres? {
        return dsl.selectFrom(GENRES)
            .where(GENRES.TITLE.likeIgnoreCase(title))
            .orderBy(GENRES.TITLE.asc())
            .fetchOne()
            ?.into(Genres::class.java)
    }

    fun save(pojo: Genres): Genres {
        val rec = dsl.newRecord(GENRES, pojo)
        rec.store()
        return rec.into(Genres::class.java)
    }

    fun saveMediaGenres(mediaId: Long, genreId: Long): MediaGenres? {
        val rec = dsl.newRecord(MEDIA_GENRES, MediaGenres(mediaId, genreId))
        rec.store()
        return rec.into(MediaGenres::class.java)
    }

}