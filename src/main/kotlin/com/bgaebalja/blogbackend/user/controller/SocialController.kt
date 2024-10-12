package com.bgaebalja.blogbackend.user.controller

import com.bgaebalja.blogbackend.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
class SocialController(private val userService: UserService) {


    @GetMapping("/kakao")
    fun getKakaoUser(accessToken: String): String {
        val kakaoEmail = userService.getKakaoUser(accessToken)?:throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED)
        println(kakaoEmail)
        return "SUCCESS"
    }

}