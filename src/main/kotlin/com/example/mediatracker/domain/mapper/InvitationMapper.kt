package com.example.mediatracker.domain.mapper

import com.example.jooq.generated.tables.pojos.Invitations as InvitationsPojo
import com.example.mediatracker.domain.entity.impl.Invitation
import org.mapstruct.Mapper
import org.mapstruct.NullValueCheckStrategy

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
)
interface InvitationMapper {

    fun toDomain(invitation: InvitationsPojo): Invitation

    fun toPojo(invitation: Invitation): InvitationsPojo

}