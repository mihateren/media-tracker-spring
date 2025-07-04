package com.example.mediatracker.domain.entity

import java.time.OffsetDateTime

abstract class BaseEntity(
    open val createdAt: OffsetDateTime? = null,
    open var updatedAt: OffsetDateTime? = null
)