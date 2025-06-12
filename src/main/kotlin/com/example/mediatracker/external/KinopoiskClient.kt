package com.example.mediatracker.external

import com.example.mediatracker.config.KinopoiskFeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "kinopoisk", url = "\${hosts.kinopoisk.url}", configuration = [KinopoiskFeignConfig::class])
interface KinopoiskClient {

    @GetMapping("api/v2.1/films/search-by-keyword", produces = ["application/json"] )
    fun searchMedia(
        @RequestParam("keyword") keyword: String,
        @RequestParam("page") page: Int = 1
    ): List<String>
}