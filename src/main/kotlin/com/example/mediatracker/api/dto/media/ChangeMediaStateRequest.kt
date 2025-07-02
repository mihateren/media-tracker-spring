package com.example.mediatracker.api.dto.media

import com.example.mediatracker.domain.entity.MediaState


class ChangeMediaStateRequest(
    val state: MediaState?,
    val rating: Int?,
    val review: String?
) {
}