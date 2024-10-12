package com.bgaebalja.blogbackend.user.service

import com.bgaebalja.blogbackend.util.createPassword
import com.bgaebalja.blogbackend.user.domain.Users
import com.bgaebalja.blogbackend.user.dto.JoinRequest
import com.bgaebalja.blogbackend.user.dto.UserDto
import com.bgaebalja.blogbackend.user.repository.UserRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.Disposable
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
        val createUser = Users.createUser(email, username, password, fullName)
        userRepository.save(createUser)
    }

    @Transactional
    override fun getKakaoUser(accessToken: String): Mono<String> {
        val webClient = WebClient.builder()
            .defaultHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
            .build()

         return webClient.get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .header(AUTHORIZATION, "Bearer ${accessToken}")
            .retrieve()
            .bodyToMono<String>()
    }

    override fun findUserWithRole(email: String): UserDto? {
        userRepository.findByEmail(email)?: return null
        val user = userRepository.findUserWithRole(email)
        return UserDto(
            user.id,
            user.email,
            user.password,
            user.userId,
            user.username,
            user.fullName,
            user.roles.map { it.role }.toMutableList()
        )
    }


}