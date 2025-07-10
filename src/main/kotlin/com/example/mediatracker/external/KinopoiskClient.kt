package com.example.mediatracker.external

import com.example.mediatracker.config.FeignConfig
import com.example.mediatracker.external.dto.MediaDetailsResponse
import com.example.mediatracker.external.dto.SearchResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "kinopoisk", url = "\${hosts.kinopoisk.url}", configuration = [FeignConfig::class])
interface KinopoiskClient {

    @GetMapping("/api/v2.1/films/search-by-keyword", produces = ["application/json"])
    fun searchMedia(
        @RequestParam("keyword") keyword: String,
        @RequestParam("page") page: Int = 1
    ): ResponseEntity<SearchResponse>

    @GetMapping("/api/v2.2/films/{id}", produces = ["application/json"])
    fun getMediaDetailsById(
        @PathVariable("id") id: Long,
    ): ResponseEntity<MediaDetailsResponse>
}