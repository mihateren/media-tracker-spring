package com.example.mediatracker.mapper

import com.example.jooq.generated.tables.pojos.Users as UsersPojo
import com.example.mediatracker.domain.entity.User
import org.mapstruct.*
import org.springframework.stereotype.Component

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT
)
interface UserMapper {

    fun toDomain(pojo: UsersPojo): User

    @Mapping(target = "id", ignore = true)
    fun toPojoNew(user: User): UsersPojo

    fun toPojoExisting(user: User): UsersPojo
}
