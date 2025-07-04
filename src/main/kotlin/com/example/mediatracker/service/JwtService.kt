package com.example.mediatracker.service

import com.example.mediatracker.common.props.JwtProperties
import com.example.mediatracker.domain.entity.user.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    props: JwtProperties
) {

    companion object {
        private const val CLAIM_TYPE = "typ"
        private const val CLAIM_USER = "username"

        private const val TYPE_ACCESS = "access"
        private const val TYPE_REFRESH = "refresh"
        private const val TYPE_INVITE = "invite"
    }

    private val authKey: SecretKey = Keys.hmacShaKeyFor(props.authSecret.toByteArray(StandardCharsets.UTF_8))
    private val inviteKey: SecretKey = Keys.hmacShaKeyFor(props.inviteSecret.toByteArray(StandardCharsets.UTF_8))

    private val accessTtl = Duration.ofMinutes(props.expiration.accessMin)
    private val refreshTtl = Duration.ofDays(props.expiration.refreshDays)
    private val inviteTtl = Duration.ofDays(props.expiration.inviteDays)


    fun generateAccessToken(user: User) = buildToken(user, accessTtl, TYPE_ACCESS, authKey)
    fun generateRefreshToken(user: User) = buildToken(user, refreshTtl, TYPE_REFRESH, authKey)
    fun generateInviteToken(user: User) = buildToken(user, inviteTtl, TYPE_INVITE, inviteKey)


    fun validateAccessToken(token: String) = validate(token, TYPE_ACCESS)
    fun validateRefreshToken(token: String) = validate(token, TYPE_REFRESH)
    fun validateInviteToken(token: String) = validate(token, TYPE_INVITE)


    fun extractUsername(token: String): String =
        claims(token).get(CLAIM_USER, String::class.java)

    fun extractUserId(token: String): Long =
        claims(token).subject.toLong()


    private fun buildToken(
        user: User,
        ttl: Duration,
        type: String,
        key: SecretKey
    ): String =
        Jwts.builder()
            .subject(user.id.toString())
            .claim(CLAIM_TYPE, type)
            .claim(CLAIM_USER, user.username)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(ttl)))
            .signWith(key, Jwts.SIG.HS512)
            .compact()

    private fun claims(token: String): Claims {
        return try {
            parseWithKey(token, authKey)
        } catch (ex: SignatureException) {
            parseWithKey(token, inviteKey)
        }
    }

    private fun parseWithKey(token: String, key: SecretKey): Claims =
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

    private fun validate(token: String, expectedType: String): Boolean = runCatching {
        val c = claims(token)
        c[CLAIM_TYPE] == expectedType
    }.getOrDefault(false)
}
