package com.example.mediatracker.domain.repository

import com.example.jooq.generated.tables.daos.UsersProfilesDao
import com.example.mediatracker.domain.entity.UserProfile
import com.example.mediatracker.domain.mapper.UserProfileMapper
import org.springframework.stereotype.Repository
import java.time.Clock
import java.time.OffsetDateTime

@Repository
class UserProfilesRepository(
    private val usersProfilesDao: UsersProfilesDao,
    private val mapper: UserProfileMapper
) {

    fun save(userProfile: UserProfile): UserProfile {
        val pojo = mapper.toPojo(userProfile);

        return if (usersProfilesDao.existsById(userProfile.userId)) {
            val currentTime = OffsetDateTime.now(Clock.systemUTC())
            pojo.updatedAt = currentTime
            usersProfilesDao.update(pojo)
            userProfile.copy(updatedAt = currentTime)
        } else {
            usersProfilesDao.insert(pojo)
            mapper.toDomain(pojo)
        }
    }
}