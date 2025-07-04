package com.example.mediatracker.domain.entity

import com.example.jooq.generated.enums.InvitationStatus
import java.time.OffsetDateTime

data class Invitation(
    val id: Long? = null,
    val inviterId: Long,
    val inviteeId: Long,
    val status: InvitationStatus? = null,
    val pairId: Long? = null,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
)