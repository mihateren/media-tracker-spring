package com.example.mediatracker.domain.mapper

import com.example.jooq.generated.tables.pojos.Users as UsersPojo
import com.example.mediatracker.domain.entity.User
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
)
interface UserMapper {

    @Mapping(target = "passwordHash", source = "password")
    fun toDomain(pojo: UsersPojo): User

    @Mapping(target = "password", source = "passwordHash")
    fun toPojo(user: User): UsersPojo

}
