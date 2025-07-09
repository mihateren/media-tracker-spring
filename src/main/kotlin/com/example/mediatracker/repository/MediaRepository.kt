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

    fun getAllMediaByPairId(pairId: Long): List<Media> =
        dsl.selectFrom(MEDIA)
            .where(PAIR_MEDIA.PAIR_ID.eq(pairId))
            .orderBy(PAIR_MEDIA.CREATED_AT)
            .fetchInto(Media::class.java)

}