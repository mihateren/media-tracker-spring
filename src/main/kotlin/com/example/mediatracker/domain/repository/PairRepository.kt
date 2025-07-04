package com.example.mediatracker.domain.repository

import com.example.jooq.generated.tables.daos.PairsDao
import com.example.mediatracker.domain.entity.pair.Pair
import com.example.mediatracker.domain.mapper.PairMapper
import org.springframework.stereotype.Repository

@Repository
class PairRepository(
    private val pairsDao: PairsDao,
    private val mapper: PairMapper,
) {

    fun savePair(pair: Pair): Pair {
        val pojo = mapper.toPojo(pair)
        return if (pair.pairId == null) {
            pairsDao.insert(pojo)
            mapper.toDomain(pojo)
        } else {
            pairsDao.update(pojo)
            pair
        }
    }

}