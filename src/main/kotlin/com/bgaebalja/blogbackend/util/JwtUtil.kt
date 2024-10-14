package com.bgaebalja.blogbackend.util

import com.bgaebalja.blogbackend.user.exception.JwtCustomException
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets.UTF_8
import java.time.ZonedDateTime
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${jwt.secret.key}")
    private var SECRETE_KEY: String
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(SECRETE_KEY.toByteArray(UTF_8))

    fun generateToken(valueMap: MutableMap<String, Any>?, min: Long): String {
        return Jwts.builder()
            .setHeader(mapOf("typ" to "JWT"))
            .setClaims(valueMap)
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
            .signWith(key)
            .compact()
    }

    fun validateJwtToken(token: String): MutableMap<String, Any>? {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body ?: null
        } catch (malformedJwtException: MalformedJwtException) {
            throw JwtCustomException("MalFormed")
        } catch (expiredJwtException: ExpiredJwtException) {
            throw JwtCustomException("Expired")
        } catch (invalidClaimException: InvalidClaimException) {
            throw JwtCustomException("Invalid")
        } catch (jwtException: JwtException) {
            throw JwtCustomException("JWTError")
        } catch (e: Exception) {
            throw JwtCustomException("Exception")
        }
    }
}
