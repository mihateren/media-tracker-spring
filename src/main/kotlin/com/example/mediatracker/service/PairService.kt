package com.example.mediatracker.service

import com.example.jooq.generated.enums.PairStatus
import com.example.jooq.generated.tables.pojos.Invitations
import com.example.jooq.generated.tables.pojos.Pairs
import com.example.mediatracker.api.dto.media.MediaDto
import com.example.mediatracker.common.exception.entity.EntityNotFoundException
import com.example.mediatracker.common.exception.entity.PairException
import com.example.mediatracker.external.KinopoiskClient
import com.example.mediatracker.repository.InvitationRepository
import com.example.mediatracker.repository.MediaRepository
import com.example.mediatracker.repository.PairRepository
import com.example.mediatracker.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PairService(
    private val pairRepository: PairRepository,
    private val mediaRepository: MediaRepository,
    private val kinopoiskClient: KinopoiskClient,
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

        if (!isUserInPair(userId, pair))
            throw PairException()

        if (pair.firstUserId == userId) {
            pair.firstUserId = null
        } else {
            pair.secondUserId = null
        }

        if (pair.firstUserId == null && pair.secondUserId == null) {
            pairRepository.deleteById(pairId)
        } else {
            pair.status = PairStatus.archived
            pairRepository.save(pair)
        }
    }

    fun getMediaList(userId: Long, pairId: Long): List<MediaDto> {
        val pair = pairRepository.getById(pairId)
            ?: throw EntityNotFoundException("Пара с id $pairId не найдена")

        if (!isUserInPair(userId, pair))
            throw PairException()

        val mediaListDb = mediaRepository.getAllMediaByPairId(pairId)
        return mediaListDb.map { media ->
            val details = kinopoiskClient.getMediaDetailsById(media.kinopoiskId)
            MediaDto(
                id = media.id!!,
                kinopoiskId = media.kinopoiskId,
                type = media.type.toString(),
                title = media.title,
                details = details
            )
        }.toMutableList()
    }


    private fun isUserInPair(userId: Long, pair: Pairs): Boolean {
        return pair.firstUserId == userId || pair.secondUserId == userId
    }
}