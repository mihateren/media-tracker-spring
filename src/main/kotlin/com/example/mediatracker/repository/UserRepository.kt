package com.example.mediatracker.repository

import com.example.mediatracker.domain.entity.User
import com.example.jooq.generated.tables.references.USERS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    val dslContext: DSLContext,
) {

    fun findByUsername(username: String): User? {
        return dslContext
            .selectFrom(USERS)
            .where(USERS.USERNAME.eq(username))
            .fetchOneInto(User::class.java)
    }

    fun save(user: User): User {
        return if (user.id == 0L) {
            val record = dslContext.insertInto(USERS)
                .set(USERS.USERNAME, user.username)
                .set(USERS.EMAIL, user.email)
                .set(USERS.PASSWORD, user.password)
                .returning(USERS.ID)
                .fetchOne()!!

            val generatedId: Long = record.id
                ?: error("Не удалось получить сгенерированный ID")

            user.copy(id = generatedId)
        } else {
            dslContext.update(USERS)
                .set(USERS.EMAIL, user.email)
                .set(USERS.PASSWORD, user.password)
                .where(USERS.ID.eq(user.id))
                .execute()
            user
        }
    }

}