package com.example.mediatracker.service

import com.example.jooq.generated.enums.PairStatus
import com.example.jooq.generated.tables.pojos.Media
import com.example.jooq.generated.tables.pojos.Pairs
import com.example.mediatracker.api.dto.media.MediaDto
import com.example.mediatracker.common.exception.entity.EntityNotFoundException
import com.example.mediatracker.common.exception.entity.KinopoiskException
import com.example.mediatracker.common.exception.entity.PairException
import com.example.mediatracker.external.KinopoiskClient
import com.example.mediatracker.repository.MediaRepository
import com.example.mediatracker.repository.PairRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.example.jooq.generated.enums.MediaType
import com.example.jooq.generated.tables.pojos.PairMedia
import com.example.mediatracker.api.dto.media.ChangeMediaStateRequest

@Service
class PairService(
    private val pairRepository: PairRepository,
    private val mediaRepository: MediaRepository,
    private val mediaService: MediaService,
    private val genreService: GenreService,
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
        pairRepository.findActivatePair(userId)

    @Transactional
    fun exitPair(userId: Long, pairId: Long): Unit {
        val pair = findPair(userId)
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
        val pair = findPair(pairId)
        if (!isUserInPair(userId, pair))
            throw PairException()

        val mediaListDb = mediaRepository.findAllMediaByPairId(pairId)
        return mediaListDb.map { media ->
            val details = mediaService.getMediaDetailsById(media.kinopoiskId)
            MediaDto(
                id = media.id!!,
                kinopoiskId = media.kinopoiskId,
                type = media.type.toString(),
                title = media.title,
                details = details!!
            )
        }.toMutableList()
    }


    @Transactional
    fun addMedia(userId: Long, pairId: Long, kpId: Int) {
        val pair = findPair(pairId)
        if (!isUserInPair(userId, pair))
            throw PairException()

        val media = mediaRepository.findByKinopoiskId(kpId)
        if (media == null) {
            val mediaDetails = mediaService.getMediaDetailsById(kpId)
                ?: throw KinopoiskException("В системе кинопоиска не нашлось медиа с id $kpId")

            val mediaTitle = mediaDetails.nameRu ?: mediaDetails.nameEn ?: mediaDetails.nameOriginal ?: "Untitled"
            val mediaType = if (mediaDetails.serial == true) MediaType.series else MediaType.film

            mediaRepository.save(
                Media(
                    kinopoiskId = kpId,
                    title = mediaTitle,
                    type = mediaType
                )
            )
            genreService.addGenresFromMedia(mediaDetails, media?.id!!)
            pairRepository.savePairMedia(
                PairMedia(
                    pairId = pairId,
                    mediaId = media.id!!,
                    addedBy = userId
                )
            )
        }
    }

    fun changeMediaState(
        userId: Long,
        pairId: Long,
        mediaId: Long,
        request: ChangeMediaStateRequest
    ): Unit {
        val pair = findPair(pairId)
        if (!isUserInPair(userId, pair)) throw PairException()

        val pairMedia = pairRepository.findByMediaId(mediaId)
            ?: throw EntityNotFoundException("Медиа с id $mediaId не найдено")

        request.mediaState?.let { pairMedia.state = it }
        request.rating?.let { pairMedia.rating = it }
        request.review?.let { pairMedia.review = it }

        pairRepository.savePairMedia(pairMedia)
    }

    fun removeMediaFromPair(userId: Long, pairId: Long, mediaId: Long): Unit {
        val pair = findPair(pairId)
        if (!isUserInPair(userId, pair)) throw PairException()

        val pairMedia = pairRepository.findByMediaId(mediaId)
            ?: throw EntityNotFoundException("Медиа с id $mediaId не найдено")
        pairRepository.deletePairMedia(pairId, mediaId)
    }

    private fun isUserInPair(userId: Long, pair: Pairs) =
        pair.firstUserId == userId || pair.secondUserId == userId

    private fun findPair(pairId: Long) =
        pairRepository.findById(pairId)
            ?: throw EntityNotFoundException("Пара с id $pairId не найдена")
}