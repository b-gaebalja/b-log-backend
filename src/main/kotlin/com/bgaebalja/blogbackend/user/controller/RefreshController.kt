package com.bgaebalja.blogbackend.user.controller

import com.bgaebalja.blogbackend.user.dto.RefreshTokenRequest
import com.bgaebalja.blogbackend.user.exception.JwtCustomException
import com.bgaebalja.blogbackend.util.generateToken
import com.bgaebalja.blogbackend.util.validateJwtToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class RefreshController {

    @PostMapping("/users/refresh")
    fun refreshToken(
        @RequestHeader("Authorization") authHeader: String?,
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

        val claims = validateJwtToken(refreshToken)
        val newAccessToken = generateToken(claims, 10)
        val newRefreshToken = when (checkTime((claims?.get("exp").toString().toLong()))) {
            true -> generateToken(claims, 60 * 24)
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
            validateJwtToken(token)
        } catch (e: JwtCustomException) {
            return e.message.equals("Expired")
        }
        return false
    }
}