package com.example.mediatracker.domain.mapper

import com.example.jooq.generated.tables.pojos.UsersProfiles as UsersProfilesPojo
import com.example.mediatracker.api.dto.user.UserDto
import com.example.jooq.generated.tables.pojos.Users as UsersPojo
import com.example.mediatracker.domain.entity.impl.User
import com.example.mediatracker.domain.entity.impl.UserFullInfo
import com.example.mediatracker.domain.entity.impl.UserProfile
import org.mapstruct.*

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
interface UserMapper {
    @Mapping(target = "passwordHash", source = "password")
    fun userToDomain(p: UsersPojo): User

    @Mapping(target = "password", source = "passwordHash")
    fun userToPojo(u: User): UsersPojo

    fun userProfileToDomain(p: UsersProfilesPojo): UserProfile
    fun userProfileToPojo(d: UserProfile): UsersProfilesPojo

    fun userFullInfoToDto(f: UserFullInfo): UserDto
}

