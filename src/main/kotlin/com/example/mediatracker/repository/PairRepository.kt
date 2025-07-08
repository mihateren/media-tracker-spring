package com.example.mediatracker.repository

import com.example.jooq.generated.enums.PairStatus
import com.example.jooq.generated.tables.pojos.Pairs
import com.example.jooq.generated.tables.references.PAIRS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import kotlin.and
import kotlin.or
import kotlin.text.set

@Repository
class PairRepository(
    private val dsl: DSLContext
) {

    fun getById(id: Long): Pairs? =
        dsl.selectFrom(PAIRS)
            .where(PAIRS.PAIR_ID.eq(id))
            .fetchOne()
            ?.into(Pairs::class.java)

    fun save(pojo: Pairs): Pairs {
        return if (pojo.pairId == null) {
            pojo.status = PairStatus.active
            val rec = dsl.newRecord(PAIRS)
            rec.from(pojo)
            rec.store()
            rec.into(Pairs::class.java)
        } else {
            dsl.update(PAIRS)
                .set(
                    mapOf(
                        PAIRS.FIRST_USER_ID to pojo.firstUserId,
                        PAIRS.SECOND_USER_ID to pojo.secondUserId,
                        PAIRS.STATUS to pojo.status,
                    )
                )
                .where(PAIRS.PAIR_ID.eq(pojo.pairId))
                .execute()
            pojo
        }
    }

    fun getActivePairs(userId: Long): List<Pairs> =
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
}