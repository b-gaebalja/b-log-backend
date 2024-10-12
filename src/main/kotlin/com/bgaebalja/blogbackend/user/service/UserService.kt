package com.bgaebalja.blogbackend.user.service

import com.bgaebalja.blogbackend.user.dto.JoinRequest
import com.bgaebalja.blogbackend.user.dto.UserDto

interface UserService {
    fun save(joinRequest: JoinRequest)

    fun getKakaoUser(accessToken: String): Any?

    fun findUserWithRole(email: String): UserDto?
}