package com.example.mediatracker.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController() {

    @PostMapping("/register")
    fun registration(
        @RequestParam("username") username: String,
        @RequestParam("password") password: String
    ): ResponseEntity<String> {
        return ResponseEntity.ok("Success registration")
    }

    @PostMapping("/login")
    fun login(
        @RequestParam("username") username: String,
        @RequestParam("password") password: String
    ): ResponseEntity<String> {
        return ResponseEntity.ok("Success login")
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestParam("refreshToken") refreshToken: String
    ): ResponseEntity<String> {
        return ResponseEntity.ok("Refresh success")
    }

}