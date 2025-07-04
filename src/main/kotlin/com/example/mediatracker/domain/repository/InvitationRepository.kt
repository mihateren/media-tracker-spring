package com.example.mediatracker.domain.repository

import com.example.jooq.generated.enums.InvitationStatus
import com.example.jooq.generated.tables.Invitations
import com.example.jooq.generated.tables.daos.InvitationsDao
import com.example.mediatracker.domain.entity.impl.Invitation
import com.example.mediatracker.domain.mapper.InvitationMapper
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class InvitationRepository(
    private val dsl: DSLContext,
    private val mapper: InvitationMapper,
) {

    private val INV = Invitations.INVITATIONS

    fun existsPending(inviterId: Long, inviteeId: Long): Boolean =
        dsl.fetchExists(
            INV,
            INV.INVITER_ID.eq(inviterId)
                .and(INV.INVITEE_ID.eq(inviteeId))
                .and(
                    INV.STATUS.eq(InvitationStatus.pending)
                )
        )

    fun save(inv: Invitation) {
        val record = dsl.newRecord(INV, mapper.toPojo(inv))
        record.store()
    }

}