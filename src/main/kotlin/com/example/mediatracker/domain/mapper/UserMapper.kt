package com.example.mediatracker.domain.mapper

import com.example.jooq.generated.tables.pojos.UsersProfiles
import com.example.mediatracker.api.dto.users.UserDto
import com.example.jooq.generated.tables.pojos.Users as UsersPojo
import com.example.mediatracker.domain.entity.user.User
import com.example.mediatracker.domain.entity.user.UserFullInfo
import com.example.mediatracker.domain.entity.user.UserProfile
import org.mapstruct.*

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

    fun userFullInfoToDto(userFullInfo: UserFullInfo): UserDto

}
