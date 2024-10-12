package com.bgaebalja.blogbackend.user.controller

import com.bgaebalja.blogbackend.user.dto.JoinRequest
import com.bgaebalja.blogbackend.user.service.UserService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("join")
    fun join(joinRequest: JoinRequest): ResponseEntity<String> {
        userService.save(joinRequest)
        return ResponseEntity.status(CREATED).body("SUCCESS")
    }

}