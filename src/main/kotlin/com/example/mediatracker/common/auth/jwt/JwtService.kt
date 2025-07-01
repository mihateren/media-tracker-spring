package com.example.mediatracker.common.auth.jwt

import com.example.mediatracker.common.auth.jwt.props.JwtProperties
import com.example.mediatracker.common.logging.Logging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class JwtService(
    private val props: JwtProperties
) : Logging {

    companion object {
        private const val BEARER = "Bearer "
        private const val CLAIM_TYPE = "typ"
        private const val TYPE_ACCESS = "access"
        private const val TYPE_REFRESH = "refresh"
    }

    private val key = Keys.hmacShaKeyFor(props.secret.toByteArray(StandardCharsets.UTF_8))


    fun generateAccessToken(username: String) =
        buildToken(username, Duration.ofMinutes(props.expiration.accessMin), TYPE_ACCESS)

    fun generateRefreshToken(username: String) =
        buildToken(username, Duration.ofDays(props.expiration.refreshDays), TYPE_REFRESH)

    fun extractUsername(token: String): String =
        parse(token).subject

    fun validateAccessToken(token: String): Boolean =
        validate(token, expectedType = TYPE_ACCESS)

    fun validateRefreshToken(token: String): Boolean =
        validate(token, expectedType = TYPE_REFRESH)


    private fun buildToken(username: String, ttl: Duration, type: String): String {
        val now = Instant.now()
        return Jwts.builder()
            .subject(username)
            .claim(CLAIM_TYPE, type)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(ttl)))
            .signWith(key, Jwts.SIG.HS512)
            .compact()
    }

    private fun validate(token: String, expectedType: String): Boolean = runCatching {
        val claims = parse(token)
        claims[CLAIM_TYPE] == expectedType && !claims.expiration.before(Date())
        /* TODO: при желании здесь же проверяем jti, black-list, audience и т. д. */
    }.getOrDefault(false)

    private fun parse(token: String): Claims =
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token.removePrefix(BEARER))
            .payload
}
