package com.bgaebalja.blogbackend.user.service

import com.bgaebalja.blogbackend.user.domain.Users
import com.bgaebalja.blogbackend.user.dto.JoinRequest
import com.bgaebalja.blogbackend.user.dto.UserDto
import com.bgaebalja.blogbackend.user.repository.UserRepository
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
) : UserService {
    @Transactional
    override fun save(joinRequest: JoinRequest) {
        val email = joinRequest.email
        val password = passwordEncoder.encode(joinRequest.password)
        val fullName = joinRequest.fullName
        val username = joinRequest.username
        Users.createUser(email, username, password, fullName).apply {
            userRepository.save(this)
        }
    }

    @Transactional
    override fun getKakaoUser(accessToken: String): Mono<String> {
        WebClient.builder()
            .defaultHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
            .build()
            .apply {
                return this.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header(AUTHORIZATION, "Bearer ${accessToken}")
                    .retrieve()
                    .bodyToMono<String>()
            }
    }

    override fun findUserWithRole(email: String): UserDto? {
        userRepository.findByEmail(email) ?: return null
        userRepository.findUserWithRole(email).apply {
            return UserDto(
                this.id!!,
                this.email,
                this.password,
                this.userId,
                this.username,
                this.fullName,
                this.roles.map { it.role }.toMutableList()
            )
        }
    }


}