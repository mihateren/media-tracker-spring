package com.example.mediatracker.api.controller

import com.example.mediatracker.api.dto.invitation.InviteByUserIdRequest
import com.example.mediatracker.common.constants.SecurityConstants
import com.example.mediatracker.common.extension.userId
import com.example.mediatracker.service.InvitationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/invitations")
@SecurityRequirement(name = SecurityConstants.BEARER_AUTH)
@Tag(name = "Invitations")
class InvitationController(
    private val invitationService: InvitationService
) {

    @PostMapping
    @Operation(summary = "Создать приглашение по userId")
    fun inviteByUserId(
        @AuthenticationPrincipal jwt: Jwt,
        @Valid @RequestBody request: InviteByUserIdRequest
    ) = invitationService.createForUser(jwt.userId(), request)

//    @GetMapping
//    @Operation(summary = "Список входящих/исходящих приглашений",
//        description = "role=inviter|invitee, status=pending|accepted…")
//    fun list(
//        @AuthenticationPrincipal jwt: Jwt,
//        status: InvitationStatusFilter?
//    ) = invitationService.list(jwt.userId, role, status)
//
//
//    @PostMapping("/{id}/accept")
//    @Operation(summary = "Принять приглашение")
//    fun accept(
//        @AuthenticationPrincipal jwt: Jwt,
//        @PathVariable id: Long
//    ) = invitationService.accept(jwt.userId, id)
//
//    @PostMapping("/{id}/reject")
//    @Operation(summary = "Отклонить приглашение")
//    fun reject(
//        @AuthenticationPrincipal jwt: Jwt,
//        @PathVariable id: Long
//    ) = invitationService.reject(jwt.userId, id)
//
//    @DeleteMapping("/{id}")
//    @Operation(summary = "Отменить приглашение (Cancel)")
//    fun cancel(
//        @AuthenticationPrincipal jwt: Jwt,
//        @PathVariable id: Long
//    ) = invitationService.cancel(jwt.userId, id)

}
