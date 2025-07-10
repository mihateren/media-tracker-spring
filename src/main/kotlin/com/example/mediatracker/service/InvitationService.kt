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
import kotlin.math.max
import kotlin.math.min

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

        if (inviterId == request.userId)
            throw InvitationException("Нельзя пригласить самого себя")

        val inviteeUser = userRepository.findById(request.userId)
            ?: throw UserNotFoundException(request.userId)

        if (invitationRepository.existsPending(inviterId, request.userId)) {
            throw InvitationException("Приглашение уже отправлено")
        }

        if (invitationRepository.existsPending(request.userId, inviterId)) {
            throw InvitationException("Вам уже отправлено приглашение от этого пользователя")
        }

        val invitation = Invitations(
            inviterId = inviterUser.id!!,
            inviteeId = inviteeUser.id!!,
        )
        invitationRepository.save(invitation)
    }

    fun getAllInvitations(userId: Long, statusDto: InvitationStatus?): List<Invitations> =
        invitationRepository.findByUserIdAndStatus(userId, statusDto)

    @Transactional
    fun acceptInvitation(userId: Long, invitationId: Long): Unit {
        val invitation = invitationRepository.findByInvitationId(invitationId)
            ?: throw InvitationException("Ошибка - приглашение не найдено")

        if (invitation.inviteeId != userId) throw AccessDeniedException()
        if (invitation.status == InvitationStatus.accepted) return

        val (firstId, secondId) = listOf(userId, invitation.inviterId).sorted()

        invitationRepository.changeStatus(invitation.id!!, InvitationStatus.accepted)
        pairService.createPair(firstId, secondId)
        invitationRepository.deleteById(invitation.id!!)
    }

    @Transactional
    fun rejectInvitation(userId: Long, invitationId: Long): Unit {
        val invitation = invitationRepository.findByInvitationId(invitationId)
            ?: throw InvitationException("Ошибка - приглашение не найдено")

        if (invitation.inviteeId != userId) throw AccessDeniedException()
        if (invitation.status == InvitationStatus.rejected) return

        invitationRepository.changeStatus(invitationId, InvitationStatus.rejected)
    }

    @Transactional
    fun cancelInvitation(userId: Long, invitationId: Long): Unit {
        val invitation = invitationRepository.findByInvitationId(invitationId)
            ?: throw InvitationException("Ошибка - приглашение не найдено")

        if (invitation.inviterId != userId) throw AccessDeniedException()
        if (invitation.status == InvitationStatus.canceled) return

        invitationRepository.changeStatus(invitationId, InvitationStatus.canceled)
    }

}