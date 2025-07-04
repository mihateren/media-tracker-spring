package com.example.mediatracker.repository

import com.example.jooq.generated.enums.InvitationStatus
import com.example.jooq.generated.tables.references.INVITATIONS
import com.example.jooq.generated.tables.pojos.Invitations
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class InvitationRepository(
    private val dsl: DSLContext
) {

    fun existsPending(inviterId: Long, inviteeId: Long): Boolean =
        dsl.fetchExists(
            INVITATIONS,
            INVITATIONS.INVITER_ID.eq(inviterId)
                .and(INVITATIONS.INVITEE_ID.eq(inviteeId))
                .and(INVITATIONS.STATUS.eq(InvitationStatus.pending))
        )

    fun save(pojo: Invitations): Invitations {
        val rec = dsl.newRecord(INVITATIONS)
        rec.from(pojo)
        rec.store()
        return rec.into(Invitations::class.java)
    }
}
