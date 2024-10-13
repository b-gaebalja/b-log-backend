package com.bgaebalja.blogbackend.user.controller

import com.bgaebalja.blogbackend.user.dto.JoinRequest
import com.bgaebalja.blogbackend.user.dto.UserDto
import com.bgaebalja.blogbackend.user.dto.UserRequest
import com.bgaebalja.blogbackend.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "회원 관련 API")
class UserController(
    private val userService: UserService,
) {

    companion object {
        private const val JOIN_USER = "회원가입"
        private const val JOIN_USER_DESCRIPTION = "회원가입을 할 수 있습니다"
        private const val JOIN_USER_FORM = "회원가입 양식"

        private const val GET_USER = "유저 조회"
        private const val GET_USER_DESCRIPTION = "카카오 계정에 등록된 이메일을 통해 유저를 조회합니다"
        private const val GET_USER_FORM = "유저 조회에 사용할 카카오 계정에 등록된 이메일"
    }

    @ApiResponse(responseCode = "201", description = "회원가입",
        content = [Content(schema = Schema(example = "[SUCCESS]"))]
    )
    @Operation(summary = JOIN_USER, description = JOIN_USER_DESCRIPTION)
    @PostMapping("join")
    fun join(@Valid
             @Parameter(description = JOIN_USER_FORM)
             @ModelAttribute joinRequest: JoinRequest)
             : ResponseEntity<String> {
        userService.save(joinRequest)
        return ResponseEntity.status(CREATED).body("SUCCESS")
    }

    @ApiResponse(responseCode = "200", description = "유저조회",
        content = [Content(schema = Schema(implementation = UserDto::class))])
    @Operation(summary = GET_USER, description = GET_USER_DESCRIPTION)
    @PostMapping("/user")
    fun getUser(
        @Valid
        @Parameter(description = GET_USER_FORM)
        @RequestBody email: UserRequest): ResponseEntity<Any> {
        val userDto = userService.findUserWithRole(email.email) ?: run {
            return ResponseEntity.status(OK).body(mapOf("ERROR" to "NOT FOUND"))
        }
        return ResponseEntity.status(OK).body(userDto)
    }

}