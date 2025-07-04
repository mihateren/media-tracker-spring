package com.example.mediatracker.domain.repository

import com.example.jooq.generated.tables.Users
import com.example.jooq.generated.tables.UsersProfiles
import com.example.jooq.generated.tables.daos.UsersDao
import com.example.jooq.generated.tables.daos.UsersProfilesDao
import com.example.mediatracker.domain.entity.impl.User
import com.example.mediatracker.domain.entity.impl.UserFullInfo
import com.example.mediatracker.domain.entity.impl.UserProfile
import com.example.mediatracker.domain.mapper.UserMapper
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val dsl: DSLContext,
    private val usersDao: UsersDao,
    private val profilesDao: UsersProfilesDao,
    private val mapper: UserMapper,
) {
    private val U = Users.USERS
    private val P = UsersProfiles.USERS_PROFILES


    fun save(user: User): User {
        val pojo = mapper.userToPojo(user)
        usersDao.merge(pojo)
        return mapper.userToDomain(pojo)
    }

    fun updateUsername(id: Long, newUsername: String): Boolean =
        dsl.update(U)
            .set(U.USERNAME, newUsername)
            .where(U.ID.eq(id))
            .execute() == 1


    fun saveProfile(profile: UserProfile): UserProfile {
        val pojo = mapper.userProfileToPojo(profile)
        profilesDao.merge(pojo)
        return mapper.userProfileToDomain(pojo)
    }


    fun findById(id: Long): User? =
        usersDao.fetchOneById(id)?.let(mapper::userToDomain)

    fun findByEmail(email: String): User? =
        usersDao.fetchOneByEmail(email)?.let(mapper::userToDomain)

    fun findByUsername(name: String): User? =
        usersDao.fetchOneByUsername(name)?.let(mapper::userToDomain)

    fun findProfileById(id: Long): UserProfile? =
        profilesDao.fetchOneByUserId(id)?.let(mapper::userProfileToDomain)

    fun existsByUsername(name: String) = usersDao.fetchOneByUsername(name) != null
    fun existsByEmail(email: String) = usersDao.fetchOneByEmail(email) != null

    fun findWithProfileById(id: Long): UserFullInfo? =
        dsl.select(
            U.ID, U.USERNAME, U.EMAIL,
            P.AVATAR_URL, P.CREATED_AT, P.UPDATED_AT, P.ENABLED
        )
            .from(U)
            .leftJoin(P).on(P.USER_ID.eq(U.ID))
            .where(U.ID.eq(id))
            .fetchOne { (uid, name, mail, avatar, created, updated, enabled) ->
                UserFullInfo(
                    id = uid!!,
                    username = name!!,
                    email = mail!!,
                    avatarUrl = avatar,
                    enabled = enabled!!
                )
            }
}
