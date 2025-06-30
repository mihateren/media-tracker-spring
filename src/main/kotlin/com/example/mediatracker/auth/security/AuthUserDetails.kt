package com.example.mediatracker.auth.security

import com.example.mediatracker.domain.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthUserDetails(private val user: User) : UserDetails {

    override fun getUsername() = user.username
    override fun getPassword() = user.passwordHash

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun isAccountNonExpired()  = true
    override fun isAccountNonLocked()   = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled()            = true
}