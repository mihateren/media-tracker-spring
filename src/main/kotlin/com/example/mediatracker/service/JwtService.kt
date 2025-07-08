package com.example.mediatracker.service

import com.example.jooq.generated.tables.pojos.Users
import com.example.mediatracker.common.auth.AuthUserDetails
import com.example.mediatracker.common.props.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(props: JwtProperties) {

    private val authKey: SecretKey = Keys.hmacShaKeyFor(props.authSecret.toByteArray(StandardCharsets.UTF_8))

    private val accessTtl = Duration.ofMinutes(props.expiration.accessMin)
    private val refreshTtl = Duration.ofDays(props.expiration.refreshDays)

    private enum class TokenType(val value: String) { ACCESS("access"), REFRESH("refresh") }

    fun generateAccessToken(user: Users): String =
        buildToken(user, accessTtl, TokenType.ACCESS)

    fun generateRefreshToken(user: Users): String =
        buildToken(user, refreshTtl, TokenType.REFRESH)


    fun validateAccessToken(token: String, user: UserDetails): Boolean =
        validateToken(token, user, TokenType.ACCESS)

    fun validateRefreshToken(token: String, user: UserDetails): Boolean =
        validateToken(token, user, TokenType.REFRESH)

    private fun buildToken(user: Users, ttl: Duration, type: TokenType): String =
        Jwts.builder()
            .subject(user.id.toString())
            .claim("typ", type.value)
            .claim("username", user.username)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(ttl)))
            .signWith(authKey, Jwts.SIG.HS512)
            .compact()

    private fun parseClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(authKey)
            .build()
            .parseSignedClaims(token)
            .payload

    private fun validateToken(token: String, userDetails: UserDetails, expectedType: TokenType): Boolean {
        val claims = parseClaims(token)
        val tokenSubject = claims.subject
        val tokenTypeClaim = claims["typ", String::class.java]

        return tokenSubject.equals(userDetails.username.toString())
                && tokenTypeClaim == expectedType.value
                && claims.expiration.after(Date())
    }

    fun getSubject(token: String): String = parseClaims(token).subject
}
