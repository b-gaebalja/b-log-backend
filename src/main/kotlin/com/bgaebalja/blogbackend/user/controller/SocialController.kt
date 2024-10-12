package com.bgaebalja.blogbackend.user.controller

import com.bgaebalja.blogbackend.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.Disposable
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/users")
class SocialController(private val userService: UserService) {


    @GetMapping("/kakao")
    fun getKakaoUser(accessToken: String): Mono<String> {
     return userService.getKakaoUser(accessToken).map { it.toString() }
    }

}