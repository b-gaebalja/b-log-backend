package com.bgaebalja.blogbackend.user.service

import com.bgaebalja.blogbackend.user.domain.Users
import com.bgaebalja.blogbackend.user.dto.DeleteUserRequest
import com.bgaebalja.blogbackend.user.dto.JoinRequest
import com.bgaebalja.blogbackend.user.dto.UserDto
import org.springframework.util.LinkedMultiValueMap
import reactor.core.Disposable
import reactor.core.publisher.Mono

interface UserService {
    fun save(joinRequest: JoinRequest)

    fun getKakaoUser(accessToken: String): Mono<String>

    fun findUserWithRole(email: String): UserDto?

    fun findUserByUserId(userId: String): Users?

    fun deleteUserMatch(userId: String, deleteUserRequest: DeleteUserRequest): Boolean

    fun deleteUserByUserId(userId: String)
}