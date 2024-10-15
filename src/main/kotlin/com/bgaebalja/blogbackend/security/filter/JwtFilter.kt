package com.bgaebalja.blogbackend.security.filter

import com.bgaebalja.blogbackend.user.dto.UserDto
import com.bgaebalja.blogbackend.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(private val jwtUtil: JwtUtil): OncePerRequestFilter() {
    private fun validatePath(path: String, pathUri: String) = path.startsWith(pathUri)

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return if (validatePath(path, "/users/")) {
            true
        } else {
            false
        }

    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val objectMapper = ObjectMapper()
        val authorizationHeader = request.getHeader(AUTHORIZATION)
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val accessToken = authorizationHeader.substring(7)
            val claims = jwtUtil.validateJwtToken(accessToken) ?: run {
                val message =
                    objectMapper.writeValueAsString(mapOf("ERROR" to "ERROR_ACCESS_TOKEN"))
                response.contentType = APPLICATION_JSON_VALUE
                response.writer.write(message)
                return
            }
            val id = claims["id"] as Long
            val email = claims["email"] as String
            val userId = claims["userId"] as String
            val username = claims["username"] as String
            val fullName = claims["fullName"] as String
            val imageUrl = claims["imageUrl"] as String
            val roles = claims["roles"] as MutableList<String?>
            val password = claims["password"] as String
            val userDto = UserDto(id, userId, email, username, fullName, password, imageUrl,roles)
            val authenticationToken =
                UsernamePasswordAuthenticationToken(userDto, password, userDto.authorities)
            SecurityContextHolder.getContext().authentication = authenticationToken
        }
        filterChain.doFilter(request, response)
    }
}

