package com.example.mediatracker.auth.jwt

import com.example.mediatracker.auth.jwt.props.JwtProperties
import com.example.mediatracker.logging.Logging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class JwtService(
    private val props: JwtProperties
): Logging {

    private val key = Keys.hmacShaKeyFor(props.secret.toByteArray(StandardCharsets.UTF_8))

    fun generateAccessToken(username: String) =
        buildToken(username, Duration.ofMinutes(props.expiration.accessMin))

    fun generateRefreshToken(username: String) =
        buildToken(username, Duration.ofDays(props.expiration.refreshDays))

    fun extractUsername(token: String): String =
        parse(token).subject

    fun isValid(token: String): Boolean =
        runCatching { !parse(token).expiration.before(Date()) }.getOrDefault(false)

    private fun buildToken(username: String, ttl: Duration): String {
        val now = Instant.now()
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(ttl)))
            .signWith(key, Jwts.SIG.HS512)
            .compact()
    }

    private fun parse(token: String): Claims =
        Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token.removePrefix(BEARER))
            .payload

    companion object {
        private const val BEARER = "Bearer "
    }
}
