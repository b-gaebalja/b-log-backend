package com.bgaebalja.blogbackend.security.handler

import com.bgaebalja.blogbackend.user.dto.UserDto
import com.bgaebalja.blogbackend.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class LoginSuccessHandler(private val jwtUtil: JwtUtil): AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val objectMapper = ObjectMapper()
        val user: UserDto = authentication?.principal as UserDto
        val claims = user.getClaims()
        val accessToken = jwtUtil.generateToken(claims, 10L)
        val refreshToken = jwtUtil.generateToken(claims, 60 * 24)
        claims["access_token"] = accessToken
        claims["refresh_token"] = refreshToken
        response?.contentType = "application/json;charset=UTF-8"
        response?.writer?.write(objectMapper.writeValueAsString(claims))
    }
}