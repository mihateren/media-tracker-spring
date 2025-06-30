package com.example.mediatracker.domain.repository

import com.example.jooq.generated.tables.daos.UsersDao
import com.example.jooq.generated.tables.pojos.Users as UsersPojo
import com.example.mediatracker.domain.entity.User
import com.example.mediatracker.mapper.UserMapper
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val usersDao: UsersDao,
    private val mapper: UserMapper
) {

    fun findByEmail(email: String): User? =
        usersDao.fetchOneByEmail(email)?.let(mapper::toDomain)

    fun findByUsername(username: String): User? =
        usersDao.fetchOneByUsername(username)?.let(mapper::toDomain)

    fun save(user: User): User =
        if (user.id == 0L) {
            usersDao.insert(mapper.toPojoNew(user))
            mapper.toDomain(usersDao.fetchOneByUsername(user.username)!!)
        } else {
            usersDao.update(mapper.toPojoExisting(user))
            user
        }

    fun existByUsername(username: String): Boolean =
        usersDao.fetchOneByUsername(username) != null

    fun existByEmail(email: String): Boolean =
        usersDao.fetchOneByEmail(email) != null
}
