package com.bgaebalja.blogbackend.user.dto

import com.bgaebalja.blogbackend.util.ApiConstant.EMAIL_EXAMPLE
import com.bgaebalja.blogbackend.util.ApiConstant.USER_EMAIL_VALUE
import io.swagger.v3.oas.annotations.media.Schema

data class UserRequest(
    @Schema(description = USER_EMAIL_VALUE, example = EMAIL_EXAMPLE)
    val email: String
)