package com.example.mediatracker.mapper

import com.example.jooq.generated.tables.pojos.Users as UsersPojo
import com.example.mediatracker.domain.entity.User
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT
)
interface UserMapper {

    @Mapping(target = "passwordHash", source = "password")
    fun toDomain(pojo: UsersPojo): User

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "passwordHash")
    fun toPojoNew(user: User): UsersPojo

    @Mapping(target = "password", source = "passwordHash")
    fun toPojoExisting(user: User): UsersPojo
}
