package com.example.mediatracker.domain.entity.impl

import com.example.mediatracker.domain.entity.BaseEntity

data class User(
    var id: Long? = null,
    var username: String,
    var email: String? = null,
    var passwordHash : String? = null,
): BaseEntity()
