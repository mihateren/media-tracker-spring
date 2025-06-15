package com.example.mediatracker.config

import com.example.mediatracker.auth.JwtAuthEntryPoint
import com.example.mediatracker.auth.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint


@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val authEntryPoint: JwtAuthEntryPoint
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .cors(Customizer.withDefaults())
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { it.authenticationEntryPoint(authEntryPoint) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                    .requestMatchers("/", "/error").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}
