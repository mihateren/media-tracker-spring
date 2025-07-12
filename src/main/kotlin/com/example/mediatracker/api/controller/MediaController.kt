package com.example.mediatracker.api.controller

import com.example.mediatracker.common.auth.AuthUserDetails
import com.example.mediatracker.common.constants.BEARER_AUTH
import com.example.mediatracker.external.dto.MediaDetailsResponse
import com.example.mediatracker.external.dto.SearchResponse
import com.example.mediatracker.service.MediaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/media")
@SecurityRequirement(name = BEARER_AUTH)
@Tag(name = "Media")
class MediaController(
    private val mediaService: MediaService
) {

    @GetMapping("/search")
    @Operation(summary = "Поиск медиа по ключевому слову")
    fun searchMedia(
        @AuthenticationPrincipal jwt: AuthUserDetails,
        @RequestParam keyword: String
    ): SearchResponse? =
        mediaService.searchMedia(keyword)

    @GetMapping("/{kinopoiskId}")
    @Operation(summary = "Детали о фильме/сериале")
    fun getDetails(
        @AuthenticationPrincipal jwt: AuthUserDetails,
        @PathVariable kinopoiskId: Int
    ): MediaDetailsResponse? =
        mediaService.getMediaDetailsById(kinopoiskId)

}