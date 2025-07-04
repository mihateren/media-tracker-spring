package com.example.mediatracker.domain.mapper

import com.example.mediatracker.api.dto.user.UserDto
import com.example.mediatracker.domain.entity.UserFullInfo
import org.mapstruct.*

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
interface UserMapper {
    fun userFullInfoToDto(f: UserFullInfo): UserDto
}

