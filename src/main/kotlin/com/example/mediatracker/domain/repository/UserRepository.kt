package com.example.mediatracker.domain.repository

import com.example.jooq.generated.tables.daos.UsersDao
import com.example.jooq.generated.tables.daos.UsersProfilesDao
import com.example.mediatracker.domain.entity.User
import com.example.mediatracker.domain.entity.UserProfile
import com.example.mediatracker.domain.mapper.UserMapper
import org.springframework.stereotype.Repository
import java.time.Clock
import java.time.OffsetDateTime

@Repository
class UserRepository(
    private val usersDao: UsersDao,
    private val mapper: UserMapper,
    private val usersProfilesDao: UsersProfilesDao
) {

    fun save(user: User): User {
        val pojo = mapper.userToPojo(user)

        return if (user.id == null) {
            usersDao.insert(pojo)
            mapper.userToDomain(pojo)
        } else {
            usersDao.update(pojo)
            user
        }
    }

    fun saveProfile(userProfile: UserProfile): UserProfile {
        val pojo = mapper.userProfileToPojo(userProfile)
        return if (usersProfilesDao.existsById(userProfile.userId)) {
            val currentTime = OffsetDateTime.now(Clock.systemUTC())
            pojo.updatedAt = currentTime
            usersProfilesDao.update(pojo)
            userProfile.copy(updatedAt = currentTime)
        } else {
            usersProfilesDao.insert(pojo)
            mapper.userProfileToDomain(pojo)
        }

    }

    fun findByEmail(email: String): User? =
        usersDao.fetchOneByEmail(email)?.let(mapper::userToDomain)

    fun findByUsername(username: String): User? =
        usersDao.fetchOneByUsername(username)?.let(mapper::userToDomain)

    fun existsByUsername(username: String) = usersDao.fetchOneByUsername(username) != null
    fun existsByEmail(email: String) = usersDao.fetchOneByEmail(email) != null
}