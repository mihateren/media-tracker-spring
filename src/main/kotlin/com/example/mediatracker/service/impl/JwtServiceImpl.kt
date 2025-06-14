package com.example.mediatracker.service.impl

import com.example.mediatracker.service.JwtService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.Instant
import javax.crypto.SecretKey
import java.util.*

@Service
class JwtServiceImpl(
    @Value("\${jwt.secret}")
    private val secretKey: String,
    @Value("\${expiration-ms:1800000}")
    private val expirationMs: Long
) : JwtService {

    private val signingKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
    }

    override fun extractUsername(token: String): String? =
        extractAllClaims(token).subject

    override fun isTokenValid(token: String, userDetails: UserDetails): Boolean =
        extractUsername(token) == userDetails.username && !isExpired(token)

    override fun generateToken(userDetails: UserDetails): String =
        Jwts.builder()
            .subject(userDetails.username)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusMillis(expirationMs)))
            .signWith(signingKey)
            .compact()


    private fun extractAllClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload

    private fun isExpired(token: String): Boolean =
        extractAllClaims(token).expiration.before(Date())
}
