package com.example.mediatracker.auth.jwt

import com.example.mediatracker.auth.security.AuthUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwt: JwtService,
    private val users: AuthUserDetailsService
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest) =
        request.servletPath.startsWith("/api/v1/auth")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val token = request.getHeader(AUTHORIZATION)
            ?.takeIf { it.startsWith(BEARER) }

        if (token == null || !jwt.validateAccessToken(token)) {
            return chain.doFilter(request, response)
        }

        if (SecurityContextHolder.getContext().authentication == null) {
            val username = jwt.extractUsername(token)
            val userDetails = users.loadUserByUsername(username)

            val auth = UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.authorities
            ).apply {
                details = WebAuthenticationDetailsSource().buildDetails(request)
            }
            SecurityContextHolder.getContext().authentication = auth
        }
        chain.doFilter(request, response)
    }

    companion object { private const val BEARER = "Bearer " }
}
