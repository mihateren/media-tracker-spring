package com.example.mediatracker.api.dto.media

import com.example.jooq.generated.enums.MediaState
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size


data class ChangeMediaStateRequest(

    val mediaState: MediaState? = null,

    @field:Min(1)
    @field:Max(10)
    val rating: Short? = null,

    @field:Size(max = 1024)
    val review: String? = null
)