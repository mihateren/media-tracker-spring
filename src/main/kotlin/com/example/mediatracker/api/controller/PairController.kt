package com.example.mediatracker.api.controller

import com.example.mediatracker.common.auth.AuthUserDetails
import com.example.mediatracker.common.constants.BEARER_AUTH
import com.example.mediatracker.service.PairService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/pairs")
@SecurityRequirement(name = BEARER_AUTH)
@Tag(name = "Pairs")
class PairController(
    private val pairService: PairService
) {

    @GetMapping
    @Operation(summary = "Список активных пар текущего пользователя")
    fun listPairs(@AuthenticationPrincipal jwt: AuthUserDetails) =
        pairService.getActiveList(jwt.userId())

    @DeleteMapping("/{pairId}")
    @Operation(summary = "Выйти из пары")
    fun exitPair(
        @AuthenticationPrincipal jwt: AuthUserDetails,
        @PathVariable pairId: Long
    ) = pairService.exitPair(jwt.userId(), pairId)

    @GetMapping("/{pairId}/media")
    fun getPairMedia(
        @PathVariable pairId: Long,
        @AuthenticationPrincipal jwt: AuthUserDetails
    ) = pairService.getMediaList(jwt.userId(), pairId)

    @PostMapping("/{pairId}/media/{kpId}")
    fun addMedia(
        @PathVariable pairId: Long,
        @PathVariable kpId: Long,
        @AuthenticationPrincipal jwt: AuthUserDetails
    ) = pairService.addMedia(jwt.userId(), pairId, kpId)

//    @PatchMapping("/{pairId}/media/{mediaId}")
//    fun changeMediaState(
//        @PathVariable pairId: Long,
//        @PathVariable mediaId: Long,
//        @RequestBody req: ChangeMediaStateRequest,
//        @AuthenticationPrincipal jwt: Jwt
//    ) = pairService.media.update(pairId, mediaId, jwt.userId, req)
//
//    @DeleteMapping("/{pairId}/media/{mediaId}")
//    fun removeMedia(
//        @PathVariable pairId: Long,
//        @PathVariable mediaId: Long,
//        @AuthenticationPrincipal jwt: Jwt
//    ) = pairService.media.remove(pairId, mediaId, jwt.userId)
}
