package com.example.mediatracker.domain.entity.pair

import com.example.jooq.generated.enums.PairStatus

data class Pair(
    val pairId: Int?,
    val firstUserId: Int,
    val secondUserId: Int,
    val status: PairStatus
) {
}