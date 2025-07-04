package com.example.mediatracker.domain.repository

import com.example.jooq.generated.tables.Users
import com.example.jooq.generated.tables.UsersProfiles
import com.example.mediatracker.domain.entity.User
import com.example.mediatracker.domain.entity.UserFullInfo
import com.example.mediatracker.domain.entity.UserProfile
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val dsl: DSLContext
) {
    private val U = Users.USERS
    private val P = UsersProfiles.USERS_PROFILES


    fun save(user: User): User {
        val rec = dsl.newRecord(U).apply {
            username = user.username
            email = user.email
            passwordHash = user.passwordHash
        }
        rec.store()
        return rec.into(User::class.java)
    }

    fun updateUsername(id: Long, newUsername: String): Boolean =
        dsl.update(U)
            .set(U.USERNAME, newUsername)
            .where(U.ID.eq(id))
            .execute() == 1


    fun saveProfile(profile: UserProfile): UserProfile {
        val rec = dsl.newRecord(P).apply {
            userId = profile.userId
            avatarUrl = profile.avatarUrl
        }
        rec.store()
        return rec.into(UserProfile::class.java)
    }


    fun existsByUsername(name: String) =
        dsl.fetchExists(U, U.USERNAME.eq(name))

    fun existsByEmail(email: String) =
        dsl.fetchExists(U, U.EMAIL.eq(email))


    fun findById(id: Long): User? =
        dsl.selectFrom(U).where(U.ID.eq(id)).fetchOne()?.into(User::class.java)

    fun findByEmail(email: String): User? =
        dsl.selectFrom(U).where(U.EMAIL.eq(email)).fetchOne()?.into(User::class.java)

    fun findByUsername(name: String): User? =
        dsl.selectFrom(U).where(U.USERNAME.eq(name)).fetchOne()?.into(User::class.java)

    fun findProfileById(id: Long): UserProfile? =
        dsl.selectFrom(P).where(P.USER_ID.eq(id)).fetchOne()?.into(UserProfile::class.java)


    fun findWithProfileById(id: Long): UserFullInfo? =
        dsl.select(
            U.ID, U.USERNAME, U.EMAIL,
            P.AVATAR_URL, P.ENABLED,
            P.CREATED_AT, P.UPDATED_AT
        )
            .from(U)
            .leftJoin(P).on(P.USER_ID.eq(U.ID))
            .where(U.ID.eq(id))
            .fetchOne { (uid,
                            uname,
                            mail,
                            avatar,
                            enabled,
                            created,
                            updated) ->

                UserFullInfo(
                    id = uid!!,
                    username = uname!!,
                    email = mail!!,
                    avatarUrl = avatar,
                    enabled = enabled!!,
                    createdAt = created,
                    updatedAt = updated
                )
            }
}
