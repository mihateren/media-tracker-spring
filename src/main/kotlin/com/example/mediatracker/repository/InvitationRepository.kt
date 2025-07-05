package com.example.mediatracker.repository

import com.example.jooq.generated.tables.references.INVITATIONS
import com.example.jooq.generated.tables.pojos.Invitations
import com.example.jooq.generated.enums.InvitationStatus
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class InvitationRepository(
    private val dsl: DSLContext
) {

    fun getByInvId(invId: Long): Invitations? =
        dsl.selectFrom(INVITATIONS)
            .where(INVITATIONS.ID.eq(invId))

    fun existsPending(inviterId: Long, inviteeId: Long): Boolean =
        dsl.fetchExists(
            INVITATIONS,
            INVITATIONS.INVITER_ID.eq(inviterId)
                .and(INVITATIONS.INVITEE_ID.eq(inviteeId))
                .and(INVITATIONS.STATUS.eq(InvitationStatus.pending))
        )

    fun save(pojo: Invitations): Invitations {
        return if (pojo.id == null) {
            if (pojo.status == null) pojo.status = InvitationStatus.pending
            val rec = dsl.newRecord(INVITATIONS)
            rec.from(pojo)
            rec.store()
            rec.into(Invitations::class.java)
        } else {
            dsl.update(INVITATIONS)
                .set(INVITATIONS.STATUS, pojo.status ?: InvitationStatus.pending)
                .set(INVITATIONS.PAIR_ID, pojo.pairId)
                .where(INVITATIONS.ID.eq(pojo.id))
                .execute()
            pojo
        }
    }

    fun findByUserIdAndStatus(userId: Long, status: InvitationStatus?): List<Invitations> =
        dsl.selectFrom(INVITATIONS)
            .where(
                INVITATIONS.INVITER_ID.eq(userId)
                    .or(INVITATIONS.INVITEE_ID.eq(userId))
            )
            .let { q ->
                if (status != null)
                    q.and(INVITATIONS.STATUS.eq(status))
                else
                    q
            }
            .orderBy(INVITATIONS.CREATED_AT.desc())
            .fetchInto(Invitations::class.java)

    fun changeStatus(invId: Long, status: InvitationStatus): Unit = run {
        dsl.update(INVITATIONS)
            .set(INVITATIONS.STATUS, status)
            .where(INVITATIONS.ID.eq(invId))
            .execute()
    }


}
