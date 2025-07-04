package com.example.mediatracker.api.controller

import com.example.mediatracker.api.dto.media.ChangeMediaStateRequest
import com.example.mediatracker.api.dto.pair.CreatePairResponse
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
    val pairService: PairService
) {

    @Deprecated("Не готово")
    @GetMapping("/current")
    @Operation(summary = "Активная пара текущего пользователя")
    fun getCurrentPair() = null

    @PostMapping("/invite")
    @Operation(summary = "Сгенерировать инвайт-токен")
    fun createInvite(@AuthenticationPrincipal jwt: Jwt): CreatePairResponse {
        return pairService.generateInviteToken(jwt.userId())
    }

    @Deprecated("Не готово")
    @PostMapping("/accept")
    @Operation(summary = "Принять приглашение")
    fun acceptInvite() = null

    @Deprecated("Не готово")
    @PostMapping("/reject")
    @Operation(summary = "Отклонить приглашение")
    fun rejectInvite() = null

    @Deprecated("Не готово")
    @DeleteMapping("/{pairId}/exit")
    @Operation(summary = "Выйти из пары")
    fun exitPair(@PathVariable pairId: String) = null

    @Deprecated("Не готово")
    @GetMapping("/{pairId}/media")
    @Operation(summary = "Все медиа пары")
    fun getPairMedia(@PathVariable pairId: String) = null

    @Deprecated("Не готово")
    @PostMapping("/{pairId}/media/{kinopoiskId}")
    @Operation(summary = "Добавить медиа")
    fun addMedia(@PathVariable pairId: String, @PathVariable kinopoiskId: String) = null

    @Deprecated("Не готово")
    @PatchMapping("/{pairId}/media/{mediaId}")
    @Operation(summary = "Изменить статус/оценку/отзыв медиа")
    fun changeMediaState(
        @PathVariable pairId: String,
        @PathVariable mediaId: String,
        @RequestBody request: ChangeMediaStateRequest
    ) = null

    @Deprecated("Не готово")
    @DeleteMapping("/{pairId}/media/{mediaId}")
    @Operation(summary = "Удалить медиа из списка")
    fun removeMedia(@PathVariable pairId: String, @PathVariable mediaId: String) = null
}