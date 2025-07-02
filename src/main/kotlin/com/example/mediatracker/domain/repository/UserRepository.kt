package com.example.mediatracker.domain.repository

import com.example.jooq.generated.tables.daos.UsersDao
import com.example.mediatracker.domain.entity.User
import com.example.mediatracker.domain.mapper.UserMapper
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val dao: UsersDao,
    private val mapper: UserMapper
) {

    fun save(user: User): User {
        val pojo = mapper.toPojo(user)

        return if (user.id == null) {
            dao.insert(pojo)
            mapper.toDomain(pojo)
        } else {
            dao.update(pojo)
            user
        }
    }

    fun findByEmail(email: String): User? =
        dao.fetchOneByEmail(email)?.let(mapper::toDomain)

    fun findByUsername(username: String): User? =
        dao.fetchOneByUsername(username)?.let(mapper::toDomain)

    fun existsByUsername(username: String) = dao.fetchOneByUsername(username) != null
    fun existsByEmail(email: String) = dao.fetchOneByEmail(email) != null
}