package com.example.mediatracker.service

import com.example.mediatracker.api.dto.invitation.InviteByUserIdRequest
import com.example.mediatracker.common.exception.entity.InvitationException
import com.example.mediatracker.common.exception.entity.UserNotFoundException
import com.example.mediatracker.domain.entity.impl.Invitation
import com.example.mediatracker.domain.repository.InvitationRepository
import com.example.mediatracker.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InvitationService(
    private val userRepository: UserRepository,
    private val invitationRepository: InvitationRepository,
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
        val invitation = Invitation(
            inviterId = inviterUser.id!!,
            inviteeId = inviteeUser.id!!
        )
        invitationRepository.save(invitation)
    }

}