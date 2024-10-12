package com.bgaebalja.blogbackend.user.exception

class JwtCustomException(s: String) : RuntimeException() {
    override val message: String?
        get() = super.message
}