package com.example.mediatracker.repository

import com.example.jooq.generated.tables.pojos.Media
import com.example.jooq.generated.tables.references.MEDIA
import com.example.jooq.generated.tables.references.PAIR_MEDIA
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class MediaRepository(
    private val dsl: DSLContext,
) {

    fun findAllMediaByPairId(pairId: Long): List<Media> =
        dsl.select(MEDIA.asterisk())
            .from(MEDIA)
            .join(PAIR_MEDIA).on(MEDIA.ID.eq(PAIR_MEDIA.MEDIA_ID))
            .where(PAIR_MEDIA.PAIR_ID.eq(pairId))
            .orderBy(PAIR_MEDIA.CREATED_AT.asc())
            .fetchInto(Media::class.java)

    fun findByKinopoiskId(kinopoiskId: Int): Media? =
        dsl.selectFrom(MEDIA)
            .where(MEDIA.KINOPOISK_ID.eq(kinopoiskId))
            .fetchOne()
            ?.into(Media::class.java)

    fun save(pojo: Media): Media {
        return if (pojo.id == null) {
            val rec = dsl.newRecord(MEDIA)
            rec.from(pojo)
            rec.store()
            rec.into(Media::class.java)
        } else {
            dsl.update(MEDIA)
                .set(dsl.newRecord(MEDIA, pojo))
                .where(MEDIA.ID.eq(pojo.id))
                .execute()
            pojo
        }
    }

    fun findById(id: Long): Media? =
        dsl.selectFrom(MEDIA)
            .where(MEDIA.ID.eq(id))
            .fetchOne()
            ?.into(Media::class.java)

}