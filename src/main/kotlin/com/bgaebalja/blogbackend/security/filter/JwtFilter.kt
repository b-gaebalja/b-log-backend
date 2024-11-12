package com.bgaebalja.blogbackend.security.filter

import com.bgaebalja.blogbackend.user.dto.UserDto
import com.bgaebalja.blogbackend.user.repository.UserRepository
import com.bgaebalja.blogbackend.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtUtil: JwtUtil,
    private val userRepository: UserRepository
    ) : OncePerRequestFilter() {
    private fun validatePath(path: String, pathUri: String) = path.startsWith(pathUri)

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        val method = request.method
        return if (validatePath(path, "/users/")) {
            true
        } else if (path.equals("/posts/users")) {
            false
        } else if (path.equals("/posts") && method.equals(HttpMethod.POST.toString())) {
            false
        } else if (validatePath(path, "/posts")) {
            true
        } else false

    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val objectMapper = ObjectMapper()
        try {
        val authorizationHeader = request.getHeader(AUTHORIZATION)
        if (authorizationHeader.startsWith("Bearer ")) {
            val accessToken = authorizationHeader.substring(7)
            val claims = jwtUtil.validateJwtToken(accessToken)?: throw JwtException("Invalid access token")
            val id = claims["id"] as Int
            val email = claims["email"] as String
            val userId = claims["userId"] as String
            val username = claims["username"] as String
            val fullName = claims["fullName"] as String
            val imageUrl = claims["imageUrl"] as String
            val roles = claims["roles"] as MutableList<String?>
            val password = userRepository.findUsersByEmail(email)?.password?: throw IllegalArgumentException("Email does not match email")
            val userDto = UserDto(id.toLong(), userId, email, username, fullName, password, imageUrl, roles)
            val authenticationToken =
                UsernamePasswordAuthenticationToken(userDto, password, userDto.authorities)
            SecurityContextHolder.getContext().authentication = authenticationToken
        }
        filterChain.doFilter(request, response)
        }catch (e: Exception){
            println("에러 $e.message")
            response.contentType = APPLICATION_JSON_VALUE
            response.writer.write(objectMapper.writeValueAsString(mapOf("ERROR" to "ERROR_ACCESS_TOKEN")))
        }
    }
}

