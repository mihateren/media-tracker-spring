package com.example.mediatracker.common.exception.entity

class InvitationException(
    override val message: String = "Invitation exception",
) : RuntimeException(message) {
}