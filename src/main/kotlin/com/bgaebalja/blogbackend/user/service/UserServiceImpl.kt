package com.bgaebalja.blogbackend.user.service

import com.bgaebalja.blogbackend.user.domain.Users
import com.bgaebalja.blogbackend.user.dto.DeleteUserRequest
import com.bgaebalja.blogbackend.user.dto.JoinRequest
import com.bgaebalja.blogbackend.user.dto.UserDto
import com.bgaebalja.blogbackend.user.repository.UserRepository
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
) : UserService {
    @Transactional
    override fun save(joinRequest: JoinRequest): Long? {
        val email = joinRequest.email
        val password = passwordEncoder.encode(joinRequest.password)
        val fullName = joinRequest.fullName
        val username = joinRequest.username
        Users.createUser(email, username, password, fullName).apply {
            val saveUser = userRepository.save(this)
            return saveUser.id
        }
    }

    @Transactional
    override fun getKakaoUser(accessToken: String): Mono<String> {
        WebClient.builder()
            .defaultHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
            .build()
            .apply {
                return this.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header(AUTHORIZATION, "Bearer ${accessToken}")
                    .retrieve()
                    .bodyToMono<String>()
            }
    }

    override fun findUserWithRole(email: String): UserDto? {
        userRepository.findByEmailAndDeleteYn(email,false) ?: return null
        userRepository.findUserWithRole(email).apply {
            return UserDto(
                this.id!!,
                this.email,
                this.password,
                this.userId,
                this.username,
                this.fullName,
                "",
                this.roles.map { it.role }.toMutableList()
            )
        }
    }

    override fun findUserByUserId(userId: String): Users {
        return userRepository.findOneByUserId(userId)
            ?: throw IllegalStateException("User not found")
    }

    override fun findUserByEmail(email: String): Boolean {
         userRepository.findUsersByEmail(email)?: return false
         return true
    }

    @Transactional
    override fun editUsername(userId: String, username: String) {
        val user =
            userRepository.findOneByUserId(userId) ?: throw IllegalStateException("User not found")
        user.username = username
    }

    @Transactional
    override fun editPassword(userId: String, password: String) {
        val user =
            userRepository.findOneByUserId(userId) ?: throw IllegalStateException("User not found")
        user.password = passwordEncoder.encode(password)
    }

    @Transactional
    override fun editUndelete(joinRequest: JoinRequest) {
        userRepository.findUsersByEmail(joinRequest.email)!!
            .apply {
                this.username = joinRequest.username
                this.password = passwordEncoder.encode(joinRequest.password)
                Users.undeleteUser(this)
            }
    }


    override fun deleteUserMatch(userId: String, deleteUserRequest: DeleteUserRequest): Boolean {
        val user: Users =
            userRepository.findOneByUserId(userId) ?: throw IllegalStateException("User not found")
        val isMatch = passwordEncoder.matches(deleteUserRequest.password, user.password)
        return if (isMatch) {
            deleteUserRequest.email == user.email
        } else false
    }

    @Transactional
    override fun deleteUserByUserId(userId: String) {
        userRepository.findOneByUserId(userId)!!
            .apply {
                Users.deleteUser(this)
            }
    }

}