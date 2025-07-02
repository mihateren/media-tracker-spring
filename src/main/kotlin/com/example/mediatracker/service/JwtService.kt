package com.example.mediatracker.service

import com.example.mediatracker.common.logging.Logging
import com.example.mediatracker.common.props.JwtProperties
import com.example.mediatracker.domain.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.Date

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


    fun generateAccessToken(user: User) =
        buildToken(user, Duration.ofMinutes(props.expiration.accessMin), TYPE_ACCESS)

    fun generateRefreshToken(user: User) =
        buildToken(user, Duration.ofDays(props.expiration.refreshDays), TYPE_REFRESH)

    fun extractUsername(token: String): String =
        parse(token).get("username", String::class.java)

    fun extractUserId(token: String): Long = parse(token).subject.toLong()

    fun validateAccessToken(token: String): Boolean =
        validate(token, expectedType = TYPE_ACCESS)

    fun validateRefreshToken(token: String): Boolean =
        validate(token, expectedType = TYPE_REFRESH)


    private fun buildToken(user: User, ttl: Duration, type: String): String {
        val now = Instant.now()
        return Jwts.builder()
            .subject(user.id.toString())
            .claim(CLAIM_TYPE, type)
            .claim("username", user.username)
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