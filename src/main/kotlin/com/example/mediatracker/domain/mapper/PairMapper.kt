package com.example.mediatracker.domain.mapper

import com.example.mediatracker.domain.entity.pair.Pair
import com.example.jooq.generated.tables.pojos.Pairs as PairsPojo
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.NullValueCheckStrategy

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
)
interface PairMapper {

    fun toDomain(pojo: PairsPojo): Pair

    fun toPojo(pair: Pair): PairsPojo

}