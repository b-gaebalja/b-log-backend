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
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw UsernameNotFoundException("Username not found")
        }
        val findUser = userRepository.findUserWithRole(username)?: throw IllegalStateException("User not found")
        return UserDto(
        findUser.id,findUser.email,findUser.password,findUser.username,findUser.username,findUser.fullName,
            findUser.roles.map { it.role }.toMutableList()
        )
    }
}