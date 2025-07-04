package com.example.mediatracker.domain.entity.impl

import com.example.jooq.generated.enums.InvitationStatus
import com.example.mediatracker.domain.entity.BaseEntity
import java.time.OffsetDateTime

data class Invitation(
    val id: Long? = null,
    val inviterId: Long,
    val inviteeId: Long,
    val status: InvitationStatus? = null
) : BaseEntity()