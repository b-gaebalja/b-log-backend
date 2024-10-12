package com.example.kotlinprojecttest.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler

class LoginFailureHandler: AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        val message = ObjectMapper().writeValueAsString(mapOf("ERROR" to "ERROR_LOGIN_FAIL"))
        response?.contentType = "application/json"
        response?.writer?.write(message)
    }
}