package com.bgaebalja.blogbackend.user.dto

import com.bgaebalja.blogbackend.util.ApiConstant.*
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile

data class JoinRequest(
    @Schema(description = USER_EMAIL_VALUE, example = EMAIL_EXAMPLE)
    var email: String,
    @Schema(description = USER_PASSWORD_VALUE, example = PASSWORD_EXAMPLE)
    val password: String,
    @Schema(description = USER_NAME_VALUE, example = USER_NAME_EXAMPLE)
    val username: String,
    @Schema(description = USER_FULLNAME_VALUE, example = FULLNAME_EXAMPLE)
    val fullName: String,
    val file: MutableList<MultipartFile> = mutableListOf()
)

private const val USER_PASSWORD_VALUE = "회원 비밀번호"
private const val PASSWORD_EXAMPLE = "10bQC7kh.j8D5IGuYwAXIC5uUXhh.UUcSfzJD6nzwhl0O00sQQdqEJq"