package com.example.mediatracker.domain.repository

import com.example.jooq.generated.tables.Users
import com.example.jooq.generated.tables.UsersProfiles
import com.example.jooq.generated.tables.daos.UsersDao
import com.example.jooq.generated.tables.daos.UsersProfilesDao
import com.example.mediatracker.domain.entity.User
import com.example.mediatracker.domain.entity.UserFullInfo
import com.example.mediatracker.domain.entity.UserProfile
import com.example.mediatracker.domain.mapper.UserMapper
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.Clock
import java.time.OffsetDateTime

@Repository
class UserRepository(
    private val usersDao: UsersDao,
    private val usersProfilesDao: UsersProfilesDao,
    private val dsl: DSLContext,
    private val mapper: UserMapper,
) {

    private val users = Users.USERS
    private val profiles = UsersProfiles.USERS_PROFILES

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

    fun findById(id: Long): User? =
        usersDao.fetchOneById(id)?.let(mapper::userToDomain)

    fun findWithProfileById(id: Long): UserFullInfo? =
        dsl.select(
            users.ID, users.USERNAME, users.EMAIL,
            profiles.AVATAR_URL, profiles.CREATED_AT, profiles.UPDATED_AT,
            profiles.ENABLED
        ).from(users)
            .leftJoin(profiles).on(profiles.USER_ID.eq(users.ID))
            .where(users.ID.eq(id))
            .fetchOne { (uid,
                            name,
                            mail,
                            avatar,
                            created,
                            updated,
                            enabled) ->
                UserFullInfo(
                    id = uid!!,
                    username = name!!,
                    email = mail!!,
                    avatarUrl = avatar,
                    createdAt = created!!,
                    updatedAt = updated,
                    enabled = enabled!!
                )
            }

    fun existsByUsername(username: String) = usersDao.fetchOneByUsername(username) != null
    fun existsByEmail(email: String) = usersDao.fetchOneByEmail(email) != null
}