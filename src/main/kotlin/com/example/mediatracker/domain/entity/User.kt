package com.example.mediatracker.domain.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
    val roles: Set<String> = setOf("USER")
) : UserDetails {

    override fun getUsername(): String = username

    override fun getPassword(): String = password

    override fun getAuthorities(): Collection<GrantedAuthority> =
        roles.map { SimpleGrantedAuthority("ROLE_$it") }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

}