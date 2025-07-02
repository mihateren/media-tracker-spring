package com.example.mediatracker.domain.mapper

import com.example.jooq.generated.tables.pojos.UsersProfiles
import com.example.jooq.generated.tables.references.USERS
import com.example.jooq.generated.tables.references.USERS_PROFILES
import com.example.mediatracker.api.dto.users.UserDto
import com.example.jooq.generated.tables.pojos.Users as UsersPojo
import com.example.mediatracker.domain.entity.User
import com.example.mediatracker.domain.entity.UserFullInfo
import com.example.mediatracker.domain.entity.UserProfile
import org.jooq.Record
import org.mapstruct.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
)
interface UserMapper {

    @Mapping(target = "passwordHash", source = "password")
    fun userToDomain(pojo: UsersPojo): User

    @Mapping(target = "password", source = "passwordHash")
    fun userToPojo(user: User): UsersPojo

    fun userProfileToDomain(pojo: UsersProfiles): UserProfile

    fun userProfileToPojo(user: UserProfile): UsersProfiles

    fun toUserFullInfo(r: Record): UserFullInfo = UserFullInfo(
        id        = r.get(USERS.ID)!!,
        username  = r.get(USERS.USERNAME)!!,
        email     = r.get(USERS.EMAIL)!!,
        avatarUrl = r.get(USERS_PROFILES.AVATAR_URL)!!,
        createdAt = r.get(USERS_PROFILES.CREATED_AT) ?: OffsetDateTime.now(),
        updatedAt = r.get(USERS_PROFILES.UPDATED_AT)!!,
        enabled   = r.get(USERS_PROFILES.ENABLED) ?: true
    )
    fun userFullInfoToDto(userFullInfo: UserFullInfo): UserDto
}
