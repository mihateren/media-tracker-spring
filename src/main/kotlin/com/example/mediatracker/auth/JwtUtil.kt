package com.example.mediatracker.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureAlgorithm
import org.bouncycastle.jcajce.BCFKSLoadStoreParameter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${jwt.secret}") val secret: String,
    @Value("\${jwt.expiration.access}") val accessTokenDuration: Long,
    @Value("\${jwt.expiration.refresh}") val refreshTokenDuration: Long
) {

    private val signingKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
    }

    fun generateAccessToken(username: String) = generateToken(username, accessTokenDuration)

    fun generateRefreshToken(username: String) = generateToken(username, refreshTokenDuration)

    fun extractUsername(token: String): String = getAllClaimsFromToken(token).subject

    fun validateToken(token: String): Boolean {
        return try {
            val claims = getAllClaimsFromToken(token)
            !claims.expiration.before(Date())
        } catch (ex: Exception) {
            false
        }
    }

    private fun generateToken(username: String, duration: Long): String {
        val now = Instant.now()
        val expiresAt = now.plusMillis(duration)

        return Jwts.builder()
            .subject(username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .signWith(signingKey, Jwts.SIG.HS512)
            .compact()
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token.removePrefix("Bearer "))
            .payload
    }
}