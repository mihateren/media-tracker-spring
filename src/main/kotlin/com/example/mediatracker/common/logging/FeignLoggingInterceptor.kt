package com.example.mediatracker.common.logging

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.slf4j.LoggerFactory
import java.util.UUID
import java.util.concurrent.TimeUnit

class FeignLoggingInterceptor : Interceptor {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun intercept(chain: Interceptor.Chain): Response {
        val reqId   = UUID.randomUUID().toString().take(8)
        val request = chain.request()

        val requestBody = request.body?.run {
            val buf = Buffer(); writeTo(buf); buf.readUtf8()
        } ?: "<empty>"

        buildString {
            append("[REQ][$reqId] ${request.method} ${request.url}\n")
            append(" FEIGN METHOD: ${request.header("X-FEIGN-METHOD") ?: "UNKNOWN"}\n")
            append(" HEADERS:\n")
            request.headers.forEach { (name, value) ->
                append("  $name: $value\n")
            }
            append(" BODY:\n")
            append("  >$requestBody<")
        }.also { log.info(it) }

        val started  = System.nanoTime()
        val response = chain.proceed(request)
        val tookMs   = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - started)

        val rawBody = response.body?.string() ?: "<empty>"

        buildString {
            append("[RES][$reqId] ${response.code} ${response.message} ($tookMs ms)\n")
            append(" FEIGN METHOD: ${request.header("X-FEIGN-METHOD") ?: "UNKNOWN"}\n")
            append(" HEADERS:\n")
            response.headers.forEach { (name, value) ->
                append("  $name: $value\n")
            }
            append(" BODY:\n")
            append("  >$rawBody<")
        }.also { log.info(it) }

        val newBody = rawBody.toByteArray()
            .toResponseBody(response.body?.contentType())

        return response.newBuilder().body(newBody).build()
    }
}
