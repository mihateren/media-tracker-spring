package com.example.mediatracker.domain.mapper

import com.example.jooq.generated.tables.pojos.UsersProfiles
import com.example.mediatracker.domain.entity.UserProfile
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT
)
interface UserProfileMapper {

    fun toDomain(pojo: UsersProfiles): UserProfile

    fun toPojo(user: UserProfile): UsersProfiles

}