package com.bgaebalja.blogbackend.user.service

import com.bgaebalja.blogbackend.util.createPassword
import com.bgaebalja.blogbackend.user.domain.Users
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

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
) : UserService {
    @Transactional
    override fun save(joinRequest: JoinRequest) {
        val email = joinRequest.email
        val password = passwordEncoder.encode(joinRequest.password)
        val fullName = joinRequest.fullName
        val username = joinRequest.username
        val createUser = Users.createUser(email, username, password, fullName)
        userRepository.save(createUser)
    }

    override fun getKakaoUser(accessToken: String): Any? {
        val webClient = WebClient.builder()
            .baseUrl("https://kapi.kakao.com/v2/user/me")
            .defaultHeader(AUTHORIZATION, "Bearer ${accessToken}")
            .defaultHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
            .build()

        val subscribe = webClient.get()
            .retrieve()
            .bodyToMono(LinkedHashMap::class.java)
            .subscribe {
                val result = it["kakao_account"]
                result as LinkedHashMap<*, *>
                val email = result["email"].toString()
                println("email: $email")
                userRepository.findByEmail(email)?: throw IllegalArgumentException("email not found")
                val createUser =
                    Users.createUser(
                        email,
                        "1111",
                        passwordEncoder.encode(createPassword()),
                        "1111"
                    )
                userRepository.save(createUser)
            }

        return subscribe
    }

    override fun findUserWithRole(email: String): UserDto? {
        val user = userRepository.findUserWithRole(email)
         return UserDto(
            user.id,
            user.email,
            user.password,
            user.userId,
            user.username,
            user.fullName,
            user.roles.map { it.role }.toMutableList()
        )
    }




}