package com.bgaebalja.blogbackend.user.service

import com.bgaebalja.blogbackend.user.dto.JoinRequest
import com.bgaebalja.blogbackend.user.dto.UserDto
import org.springframework.util.LinkedMultiValueMap
import reactor.core.Disposable
import reactor.core.publisher.Mono

interface UserService {
    fun save(joinRequest: JoinRequest)

    fun getKakaoUser(accessToken: String): Mono<String>

    fun findUserWithRole(email: String): UserDto?
}