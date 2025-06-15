package com.example.mediatracker.domain.entity

import kotlin.jvm.JvmField
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
    val id: Long,
    @JvmField
    val username: String,
    val email: String,
    @JvmField
    val password: String,
    val roles: Set<String> = setOf("USER")
) : UserDetails {

    override fun getUsername(): String = username
    override fun getPassword(): String = password

    override fun getAuthorities(): Collection<GrantedAuthority> =
        roles.map { SimpleGrantedAuthority("ROLE_$it") }

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}
