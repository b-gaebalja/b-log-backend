package com.bgaebalja.blogbackend.user.service

import com.bgaebalja.blogbackend.image.domain.TargetType.*
import com.bgaebalja.blogbackend.image.service.ImageService
import com.bgaebalja.blogbackend.user.dto.UserDto
import com.bgaebalja.blogbackend.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository,
    private val imageService: ImageService
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw UsernameNotFoundException("Username not found")
        }
        userRepository.findByEmailAndDeleteYn(username, false)
            ?: throw UsernameNotFoundException("User not found")
        userRepository.findUserWithRole(username).apply {
            val image = imageService.getImages(USER, this.id)
                .ifEmpty { throw UsernameNotFoundException("Image not found") }
            val imageUrl = image[0].s3Url
            return UserDto(
                this.id!!, this.email, this.password, this.userId, this.username, this.fullName,
                imageUrl, this.roles.map { it.role }.toMutableList()
            )
        }
    }
}