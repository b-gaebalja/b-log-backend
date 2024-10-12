package com.bgaebalja.blogbackend.security.filter

import com.bgaebalja.blogbackend.util.validateJwtToken
import com.bgaebalja.blogbackend.user.dto.UserDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter: OncePerRequestFilter() {
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
        val authorizationHeader = request.getHeader("Authorization")
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val accessToken = authorizationHeader.substring(7)
            val claims = validateJwtToken(accessToken) ?: run {
                val message =
                    objectMapper.writeValueAsString(mapOf("ERROR" to "ERROR_ACCESS_TOKEN"))
                response.contentType = "application/json"
                response.writer.write(message)
                return
            }
            val id = claims["id"]
            val email = claims["email"]
            val userId = claims["userId"]
            val username = claims["username"]
            val fullName = claims["fullName"]
            val roles = claims["roles"] as MutableList<String?>
            val password = claims["password"]
            val userDto = UserDto(
                id, userId.toString(),
                email.toString(), username.toString(), fullName.toString(),
                password.toString(), roles
            )
            val authenticationToken =
                UsernamePasswordAuthenticationToken(userDto, password, userDto.authorities)
            SecurityContextHolder.getContext().authentication = authenticationToken
        }
        filterChain.doFilter(request, response)
    }
}

