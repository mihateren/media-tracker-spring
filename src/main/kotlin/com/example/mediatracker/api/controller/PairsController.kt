package com.example.mediatracker.api.controller

import com.example.mediatracker.api.dto.media.ChangeMediaStateRequest
import com.example.mediatracker.common.constants.SecurityConstants
import com.example.mediatracker.common.exception.entity.InvalidTokenException
import com.example.mediatracker.common.extension.userId
import com.example.mediatracker.service.PairService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.websocket.server.PathParam
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/pairs")
@SecurityRequirement(name = SecurityConstants.BEARER_AUTH)
@Tag(name = "Pairs")
class PairsController(
    private val pairService: PairService
) {

//    @GetMapping
//    @Operation(summary = "Список активных пар текущего пользователя")
//    fun listPairs(@AuthenticationPrincipal jwt: Jwt) =
//        pairService.listActive(jwt.userId)
//
//    @DeleteMapping("/{pairId}")
//    @Operation(summary = "Выйти из пары")
//    fun exitPair(
//        @AuthenticationPrincipal jwt: Jwt,
//        @PathVariable pairId: Long
//    ) = pairService.exit(pairId, jwt.userId)
//
//
//    @GetMapping("/{pairId}/media")
//    fun getPairMedia(
//        @PathVariable pairId: Long,
//        @AuthenticationPrincipal jwt: Jwt
//    ) = pairService.media.list(pairId, jwt.userId)
//
//    @PostMapping("/{pairId}/media/{kpId}")
//    fun addMedia(
//        @PathVariable pairId: Long,
//        @PathVariable kpId: Long,
//        @AuthenticationPrincipal jwt: Jwt
//    ) = pairService.media.add(pairId, kpId, jwt.userId)
//
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
