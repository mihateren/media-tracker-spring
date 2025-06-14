package com.example.mediatracker.auth

import com.example.mediatracker.service.JwtService
import com.example.mediatracker.service.impl.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    val jwtService: JwtService,
    val userService: UserService
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        val jwt = authHeader?.takeIf { it.startsWith("Bearer ") }?.substringAfter("Bearer ")

        if (jwt != null && SecurityContextHolder.getContext().authentication == null) {
            val username = jwtService.extractUsername(jwt)
            if (username == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token")
                return
            }

            val userDetails = userService.getInfoByUsername(username)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        filterChain.doFilter(request, response)
    }


}