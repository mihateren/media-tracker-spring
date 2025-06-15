package com.example.mediatracker.auth

import com.example.mediatracker.service.CustomUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")

        if (token == null || !jwtUtil.validateToken(token)) {
            return filterChain.doFilter(request, response)
        }

        if (SecurityContextHolder.getContext().authentication != null) {
            return filterChain.doFilter(request, response)
        }

        val username = jwtUtil.extractUsername(token)
        val userDetails = userDetailsService.loadUserByUsername(username)

        val auth = UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.authorities
        ).apply {
            details = WebAuthenticationDetailsSource().buildDetails(request)
        }

        SecurityContextHolder.getContext().authentication = auth
        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest) =
        request.servletPath.startsWith("/api/v1/auth")
}
