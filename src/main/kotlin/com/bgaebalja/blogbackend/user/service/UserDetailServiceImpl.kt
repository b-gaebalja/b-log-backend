package com.bgaebalja.blogbackend.user.service

import com.bgaebalja.blogbackend.user.dto.UserDto
import com.bgaebalja.blogbackend.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw UsernameNotFoundException("Username not found")
        }
        userRepository.findByEmail(username) ?: throw UsernameNotFoundException("User not found")
        userRepository.findUserWithRole(username).apply {
            return UserDto(
                this.id!!, this.email, this.password, this.userId, this.username, this.fullName,
                this.roles.map { it.role }.toMutableList()
            )
        }
    }
}