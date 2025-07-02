package com.example.mediatracker.common.logging

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.util.UUID
import java.util.concurrent.TimeUnit

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class ApiLoggingFilter : OncePerRequestFilter() {

    private val mvc = PathPatternRequestMatcher.withDefaults()
    private val excluded = listOf(
        mvc.matcher("/swagger-ui/**"),
        mvc.matcher("/v3/api-docs/**"),
        mvc.matcher("/swagger-resources/**"),
        mvc.matcher("/webjars/**")
    )

    private val log = LoggerFactory.getLogger(ApiLoggingFilter::class.java)

    override fun shouldNotFilter(request: HttpServletRequest): Boolean =
        excluded.any { it.matches(request) }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val reqId = UUID.randomUUID().toString().take(8)

        val wrappedRequest = ContentCachingRequestWrapper(request, 1024 * 1024)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        val started = System.nanoTime()
        try {
            chain.doFilter(wrappedRequest, wrappedResponse)
        } finally {
            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - started)

            val requestBody = bodyAsString(wrappedRequest.contentAsByteArray)
            val responseBody = bodyAsString(wrappedResponse.contentAsByteArray)

            log.info("[REQ][$reqId] ${wrappedRequest.method} ${wrappedRequest.requestURI}\n BODY:\n >$requestBody<")
            log.info("[RES][$reqId] ${wrappedResponse.status} (${tookMs} ms)\n BODY:\n >$responseBody<")

            wrappedResponse.copyBodyToResponse()
        }
    }

    private fun bodyAsString(bytes: ByteArray): String =
        bytes.decodeToString().let { if (it.length > 5_000) it.take(5_000) + "…(truncated)" else it }
}
