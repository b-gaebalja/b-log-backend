package com.bgaebalja.blogbackend.user.controller

import com.bgaebalja.blogbackend.user.dto.*
import com.bgaebalja.blogbackend.user.exception.DeleteUserFailException
import com.bgaebalja.blogbackend.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
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

    @ApiResponse(
        responseCode = "201", description = "회원가입",
        content = [Content(schema = Schema(example = "[SUCCESS]"))]
    )
    @Operation(summary = JOIN_USER, description = JOIN_USER_DESCRIPTION)
    @PostMapping("join")
    fun join(
        @Valid
        @Parameter(description = JOIN_USER_FORM)
        @ModelAttribute joinRequest: JoinRequest
    )
            : ResponseEntity<Map<String,String>> {
        return when (userService.findUserByEmail(joinRequest.email)) {
            true -> ResponseEntity.status(OK).body(mapOf("FAIL" to "ALREADY_USER"))
            false -> {
                val userId = userService.save(joinRequest)
                return ResponseEntity.status(CREATED).body(mapOf("SUCCESS" to "$userId"))
            }
        }

    }

    @ApiResponse(
        responseCode = "200", description = "유저조회",
        content = [Content(schema = Schema(implementation = UserDto::class))]
    )
    @Operation(summary = GET_USER, description = GET_USER_DESCRIPTION)
    @PostMapping("/user")
    fun getUser(
        @Valid
        @Parameter(description = GET_USER_FORM)
        @RequestBody email: UserRequest
    ): ResponseEntity<Any> {
        val userDto = userService.findUserWithRole(email.email) ?: run {
            return ResponseEntity.status(OK).body(mapOf("ERROR" to "NOT FOUND"))
        }
        return ResponseEntity.status(OK).body(userDto)
    }

    @PatchMapping("{userId}/username")
    fun editUsername(
        @PathVariable("userId") userId: String,
        @RequestBody usernameRequest: UsernameRequest
    )
            : ResponseEntity<String> {
        userService.editUsername(userId, usernameRequest.username)
        return ResponseEntity.status(OK).body("EDIT_USERNAME_SUCCESS")
    }

    @PatchMapping("{userId}/password")
    fun editPassword(
        @PathVariable("userId") userId: String,
        @RequestBody passwordRequest: PasswordRequest
    )
            : ResponseEntity<String> {
        userService.editPassword(userId, passwordRequest.password)
        return ResponseEntity.status(OK).body("EDIT_PASSWORD_SUCCESS")
    }

    @PostMapping("/reuser")
    fun rejoinUser(@ModelAttribute joinRequest: JoinRequest): ResponseEntity<Map<String,String>>{
        userService.editUndelete(joinRequest)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mapOf("SUCCESS" to "REJOIN"))
    }

    @ApiResponse(
        responseCode = "200", description = "회원탈퇴",
        content = [Content(schema = Schema(example = "[DELETE_USER_SUCCESS]"))]
    )
    @Operation(summary = DELETE_USER, description = DELETE_USER_DESCRIPTION)
    @DeleteMapping("/{userId}")
    fun deleteUser(
        @Parameter(description = USER_ID)
        @PathVariable userId: String,
        @Parameter(description = DELETE_USER_FORM)
        @RequestBody deleteUserRequest: DeleteUserRequest
    ): ResponseEntity<String> {
        userService.deleteUserMatch(userId, deleteUserRequest).apply {
            if (this) userService.deleteUserByUserId(userId)
            else
                throw DeleteUserFailException("회원 탈퇴에 실패했습니다.")
        }
        return ResponseEntity.status(OK).body("DELETE_USER_SUCCESS")
    }


}

private const val USER_ID = "회원 조회를 위한 유저 아이디"
private const val JOIN_USER = "회원가입"
private const val JOIN_USER_DESCRIPTION = "회원가입을 할 수 있습니다"
private const val JOIN_USER_FORM = "회원가입 양식"

private const val GET_USER = "회원 조회"
private const val GET_USER_DESCRIPTION = "카카오 계정에 등록된 이메일을 통해 유저를 조회합니다"
private const val GET_USER_FORM = "회원 조회에 사용할 카카오 계정에 등록된 이메일"

private const val DELETE_USER = "회원 탈퇴"
private const val DELETE_USER_DESCRIPTION = "카카오 계정에 등록된 이메일과 비밀번호로 회원을 조회하여 검증 후 탈퇴 가능합니다"
private const val DELETE_USER_FORM = "회원 탈퇴 검증을 위한 카카오 계정에 등록된 이메일과 가입시 입력한 비밀번호"

