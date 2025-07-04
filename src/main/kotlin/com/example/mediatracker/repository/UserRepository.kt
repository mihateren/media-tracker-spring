package com.example.mediatracker.repository

import com.example.jooq.generated.tables.pojos.Users
import com.example.jooq.generated.tables.pojos.UsersProfiles
import com.example.jooq.generated.tables.references.USERS
import com.example.jooq.generated.tables.references.USERS_PROFILES
import com.example.mediatracker.api.dto.user.UserDto
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val dsl: DSLContext
) {

    fun save(user: Users): Users {
        return if (user.id == null) {
            val rec = dsl.newRecord(USERS)
            rec.from(user)
            rec.store()
            rec.into(Users::class.java)
        } else {
            dsl.update(USERS)
                .set(USERS.USERNAME, user.username)
                .set(USERS.EMAIL, user.email)
                .set(USERS.PASSWORD_HASH, user.passwordHash)
                .where(USERS.ID.eq(user.id))
                .execute()
            user
        }
    }


    fun existsByUsername(name: String) =
        dsl.fetchExists(USERS, USERS.USERNAME.eq(name))

    fun existsByEmail(email: String) =
        dsl.fetchExists(USERS, USERS.EMAIL.eq(email))

    fun findById(id: Long): Users? =
        dsl.selectFrom(USERS).where(USERS.ID.eq(id)).fetchOne()?.into(Users::class.java)

    fun findByEmail(email: String): Users? =
        dsl.selectFrom(USERS).where(USERS.EMAIL.eq(email)).fetchOne()?.into(Users::class.java)

    fun findByUsername(name: String): Users? =
        dsl.selectFrom(USERS).where(USERS.USERNAME.eq(name)).fetchOne()?.into(Users::class.java)

    fun updateUsername(id: Long, newUsername: String): Boolean =
        dsl.update(USERS)
            .set(USERS.USERNAME, newUsername)
            .where(USERS.ID.eq(id))
            .execute() == 1


    fun saveProfile(profile: UsersProfiles): UsersProfiles {
        return if (dsl.fetchExists(USERS_PROFILES, USERS_PROFILES.USER_ID.eq(profile.userId))) {
            dsl.update(USERS_PROFILES)
                .set(USERS_PROFILES.AVATAR_URL, profile.avatarUrl)
                .set(USERS_PROFILES.ENABLED, profile.enabled)
                .where(USERS_PROFILES.USER_ID.eq(profile.userId))
                .execute()
            profile
        } else {
            val rec = dsl.newRecord(USERS_PROFILES)
            rec.from(profile)
            rec.store()
            rec.into(UsersProfiles::class.java)
        }
    }

    fun findProfileById(id: Long): UsersProfiles? =
        dsl.selectFrom(USERS_PROFILES)
            .where(USERS_PROFILES.USER_ID.eq(id))
            .fetchOne()?.into(UsersProfiles::class.java)

    fun findWithProfileById(id: Long): UserDto? =
        dsl.select(
            USERS.ID,
            USERS.USERNAME,
            USERS.EMAIL,
            USERS_PROFILES.AVATAR_URL,
            USERS_PROFILES.ENABLED,
            USERS_PROFILES.CREATED_AT,
            USERS_PROFILES.UPDATED_AT
        )
            .from(USERS)
            .leftJoin(USERS_PROFILES).on(USERS_PROFILES.USER_ID.eq(USERS.ID))
            .where(USERS.ID.eq(id))
            .fetchOne { (uid,
                            uname,
                            mail,
                            avatar,
                            enabled,
                            created,
                            updated) ->

                UserDto(
                    id = uid!!,
                    username = uname!!,
                    email = mail!!,
                    avatarUrl = avatar,
                    createdAt = created!!,
                    updatedAt = updated,
                    enabled = enabled ?: true
                )
            }

}
