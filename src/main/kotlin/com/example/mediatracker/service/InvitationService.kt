package com.example.mediatracker.service

import com.example.jooq.generated.enums.InvitationStatus
import com.example.mediatracker.repository.InvitationRepository
import com.example.jooq.generated.tables.pojos.Invitations
import com.example.mediatracker.api.dto.invitation.InviteByUserIdRequest
import com.example.mediatracker.common.exception.entity.AccessDeniedException
import com.example.mediatracker.common.exception.entity.InvitationException
import com.example.mediatracker.common.exception.entity.UserNotFoundException
import com.example.mediatracker.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InvitationService(
    private val userRepository: UserRepository,
    private val invitationRepository: InvitationRepository,
    private val pairService: PairService,
) {

    @Transactional
    fun createForUser(inviterId: Long, request: InviteByUserIdRequest) {
        val inviterUser = userRepository.findById(inviterId)
            ?: throw UserNotFoundException(inviterId)

        val inviteeId = request.userId
        if (inviterId == inviteeId)
            throw InvitationException("Нельзя пригласить самого себя")

        val inviteeUser = userRepository.findById(inviteeId)
            ?: throw UserNotFoundException(inviterId)

        if (invitationRepository.existsPending(inviterId, inviteeId))
            throw InvitationException("Приглашение уже отправлено")

        val invitation = Invitations(
            inviterId = inviterUser.id!!,
            inviteeId = inviteeUser.id!!,
        )
        invitationRepository.save(invitation)
    }

    fun getAllInvitations(userId: Long, statusDto: InvitationStatus?): List<Invitations> =
        invitationRepository.findByUserIdAndStatus(userId, statusDto)

    // TODO создать запись в таблице пары + удалить приглос
    @Transactional
    fun acceptInvitation(userId: Long, inviterId: Long): Unit {
        val invitation = invitationRepository.getByInviterId(inviterId)
            ?: throw InvitationException("Ошибка - приглашение не найдено")

        if (invitation.inviteeId != userId) throw AccessDeniedException()
        if (invitation.status == InvitationStatus.accepted) return

        invitationRepository.changeStatus(inviterId, InvitationStatus.accepted)
        pairService.createPair(inviterId, invitation.inviteeId!!)

        invitationRepository.deleteById(invitation.id!!)
    }

    @Transactional
    fun rejectInvitation(userId: Long, inviterId: Long): Unit {
        val invitation = invitationRepository.getByInviterId(inviterId)
            ?: throw InvitationException("Ошибка - приглашение не найдено")

        if (invitation.inviteeId != userId) throw AccessDeniedException()
        if (invitation.status == InvitationStatus.rejected) return

        invitationRepository.changeStatus(inviterId, InvitationStatus.rejected)
    }

    @Transactional
    fun cancelInvitation(userId: Long, inviterId: Long): Unit {
        val invitation = invitationRepository.getByInviterId(inviterId)
            ?: throw InvitationException("Ошибка - приглашение не найдено")

        if (invitation.inviterId != userId) throw AccessDeniedException()
        if (invitation.status == InvitationStatus.canceled) return

        invitationRepository.changeStatus(inviterId, InvitationStatus.canceled)
    }

}