package com.example.mediatracker.config

import com.example.jooq.generated.tables.daos.PairsDao
import com.example.jooq.generated.tables.daos.UsersDao
import com.example.jooq.generated.tables.daos.UsersProfilesDao
import org.jooq.DSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DaoConfig(
    private val dslContext: DSLContext
) {

    @Bean
    fun usersDao() = UsersDao(dslContext.configuration())

    @Bean
    fun usersProfilesDao() = UsersProfilesDao(dslContext.configuration())

    @Bean
    fun pairsDao() = PairsDao(dslContext.configuration())

}