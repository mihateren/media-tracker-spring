package com.example.mediatracker.service

import com.example.jooq.generated.enums.PairStatus
import com.example.jooq.generated.tables.pojos.Invitations
import com.example.jooq.generated.tables.pojos.Pairs
import com.example.mediatracker.common.exception.entity.EntityNotFoundException
import com.example.mediatracker.common.exception.entity.PairException
import com.example.mediatracker.repository.InvitationRepository
import com.example.mediatracker.repository.PairRepository
import com.example.mediatracker.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PairService(
    private val userRepository: UserRepository,
    private val pairRepository: PairRepository
) {

    @Transactional
    fun createPair(firstId: Long, secondId: Long): Unit {
        val pair = Pairs(
            firstUserId = firstId,
            secondUserId = secondId,
        )
        pairRepository.save(pair)
    }

    fun getActiveList(userId: Long): List<Pairs> =
        pairRepository.getActivePairs(userId)


    @Transactional
    fun exitPair(userId: Long, pairId: Long): Unit {
        val pair = pairRepository.getById(pairId)
            ?: throw EntityNotFoundException("Пара с id $pairId не найдена")

        if (pair.firstUserId == userId) {
            pair.firstUserId = null
        } else if (pair.secondUserId == userId) {
            pair.secondUserId = null
        } else {
            throw PairException()
        }

        if (pair.firstUserId == null && pair.secondUserId == null) {
            pairRepository.deleteById(pairId)
        } else {
            pair.status = PairStatus.archived
            pairRepository.save(pair)
        }
    }


}