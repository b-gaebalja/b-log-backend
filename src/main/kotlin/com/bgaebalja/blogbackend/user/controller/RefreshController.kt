package com.bgaebalja.blogbackend.user.controller

import com.bgaebalja.blogbackend.exception.JwtCustomException
import com.bgaebalja.blogbackend.user.dto.RefreshTokenRequest
import com.bgaebalja.blogbackend.util.JwtUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.*

private const val VALIDATE_TOKEN = "JWT 토큰 재발급"
private const val VALIDATE_TOKEN_DESCRIPTION = "access 토큰과 refresh 토큰을 받아 검증 후 재발급 합니다"
private const val ACCESS_TOKEN = "재발급 요청한 회원의 access 토큰"
private const val REFRESH_TOKEN = "재발급 요청한 회원의 refresh 토큰"

@Tag(name = "JWT token Refresh Controller", description = "JWT 토큰 재발급 관련 API")
@RestController
class RefreshController(private val jwtUtil: JwtUtil) {


    @Operation(summary = VALIDATE_TOKEN, description = VALIDATE_TOKEN_DESCRIPTION)
    @PostMapping("/users/refresh")
    fun refreshToken(
        @Parameter(description = ACCESS_TOKEN)
        @RequestHeader("Authorization") authHeader: String?,
        @Parameter(description = REFRESH_TOKEN)
        @RequestBody refreshTokenRequest: RefreshTokenRequest
    ): Map<String, Any> {
        val refreshToken = refreshTokenRequest.refreshToken ?: throw JwtCustomException("Refresh token is invalid")
        authHeader ?: throw JwtCustomException("Auth header is invalid")
        if (authHeader.length < 7 || !authHeader.startsWith("Bearer ")) {
            throw JwtCustomException("Auth header is invalid")
        }
        val accessToken = authHeader.substring(7)
        if (!checkExpiredToken(accessToken)) {
            return mapOf(
                "accessToken" to accessToken,
                "refreshToken" to refreshToken
            )
        }

        val claims = jwtUtil.validateJwtToken(refreshToken)
        val newAccessToken = jwtUtil.generateToken(claims, 10L)
        val newRefreshToken = when (checkTime((claims?.get("exp").toString().toLong()))) {
            true -> jwtUtil.generateToken(claims, 60 * 24)
            false -> refreshToken
        }
        return mapOf("accessToken" to newAccessToken, "refreshToken" to newRefreshToken)
    }

    private fun checkTime(expiration: Long): Boolean {
        Date(expiration * 1000).apply {
            val gap = this.time - System.currentTimeMillis()
            val leftMinute = gap / (60 * 1000)
            return leftMinute < 60
        }
    }

    private fun checkExpiredToken(token: String): Boolean {
        try {
            jwtUtil.validateJwtToken(token)
        } catch (e: JwtCustomException) {
            return e.message.equals("Expired")
        }
        return false
    }
}