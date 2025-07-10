package com.example.mediatracker.repository

import com.example.jooq.generated.enums.PairStatus
import com.example.jooq.generated.tables.pojos.PairMedia
import com.example.jooq.generated.tables.pojos.Pairs
import com.example.jooq.generated.tables.references.MEDIA
import com.example.jooq.generated.tables.references.PAIRS
import com.example.jooq.generated.tables.references.PAIR_MEDIA
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class PairRepository(
    private val dsl: DSLContext
) {

    fun findById(id: Long): Pairs? =
        dsl.selectFrom(PAIRS)
            .where(PAIRS.PAIR_ID.eq(id))
            .fetchOne()
            ?.into(Pairs::class.java)

    fun save(pojo: Pairs): Pairs {
        return if (pojo.pairId == null) {
            pojo.status = PairStatus.active
            val rec = dsl.newRecord(PAIRS, pojo)
            rec.store()
            rec.into(Pairs::class.java)
        } else {
            val record = dsl.newRecord(PAIRS, pojo)
            dsl.update(PAIRS)
                .set(record)
                .where(PAIRS.PAIR_ID.eq(pojo.pairId))
                .execute()
            pojo
        }
    }

    fun findActivatePair(userId: Long): List<Pairs> =
        dsl.selectFrom(PAIRS)
            .where(
                PAIRS.STATUS.eq(PairStatus.active)
                    .and(
                        PAIRS.FIRST_USER_ID.eq(userId)
                            .or(PAIRS.SECOND_USER_ID.eq(userId))
                    )
            )
            .orderBy(PAIRS.PAIR_ID)
            .fetchInto(Pairs::class.java)

    fun deleteById(id: Long) {
        dsl.deleteFrom(PAIRS)
            .where(
                PAIRS.PAIR_ID.eq(id)
            )
            .execute()
    }

    fun findByMediaId(mediaId: Long): PairMedia? =
        dsl.selectFrom(PAIR_MEDIA)
            .where(PAIR_MEDIA.MEDIA_ID.eq(mediaId))
            .fetchOne()
            ?.into(PairMedia::class.java)

    fun savePairMedia(pojo: PairMedia): PairMedia {
        if (pojo.pairId == 0L || pojo.mediaId == 0L) {
            val record = dsl.newRecord(PAIR_MEDIA).apply { from(pojo) }
            record.store()
            return record.into(PairMedia::class.java)
        }

        val inserted = dsl.insertInto(PAIR_MEDIA)
            .set(PAIR_MEDIA.PAIR_ID, pojo.pairId)
            .set(PAIR_MEDIA.MEDIA_ID, pojo.mediaId)
            .onConflictDoNothing()
            .returning()
            .fetchOptional()

        val record = inserted.orElseGet {
            dsl.selectFrom(PAIR_MEDIA)
                .where(
                    PAIR_MEDIA.PAIR_ID.eq(pojo.pairId)
                        .and(PAIR_MEDIA.MEDIA_ID.eq(pojo.mediaId))
                )
                .fetchSingle()
        }
        return record.into(PairMedia::class.java)

    }
}