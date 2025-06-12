package com.example.mediatracker.api.controller

import com.example.mediatracker.api.dto.MediaDto
import com.example.mediatracker.service.MediaService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/media")
class MediaController(
    private val mediaService: MediaService
) {

    @GetMapping
    fun getAllMedia(): List<MediaDto> = emptyList()

    @GetMapping(params = ["keyword"], path = ["/search"])
    fun searchMedia(@RequestParam keyword: String): List<String> =
        mediaService.searchMedia(keyword)
}