package com.bgaebalja.blogbackend.user.dto

import com.bgaebalja.blogbackend.util.ApiConstant.EMAIL_EXAMPLE
import com.bgaebalja.blogbackend.util.ApiConstant.USER_EMAIL_VALUE
import io.swagger.v3.oas.annotations.media.Schema

data class DeleteUserRequest(
    @Schema(description = USER_EMAIL_VALUE, example = EMAIL_EXAMPLE)
    var email: String,
    @Schema(description = USER_PASSWORD_VALUE, example = PASSWORD_EXAMPLE)
    val password: String
)

private const val USER_PASSWORD_VALUE = "회원 비밀번호"
private const val PASSWORD_EXAMPLE = "10bQC7kh.j8D5IGuYwAXIC5uUXhh.UUcSfzJD6nzwhl0O00sQQdqEJq"