package com.example.mediatracker.api.controller

import com.example.mediatracker.api.dto.MediaDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/media")
class MediaController {

    @GetMapping()
    fun getAllMedia(): List<MediaDto> = emptyList();

}