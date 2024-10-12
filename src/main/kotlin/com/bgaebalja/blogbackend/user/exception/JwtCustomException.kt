package com.example.kotlinprojecttest.user.exception

class JwtCustomException(s: String) : RuntimeException() {
    override val message: String?
        get() = super.message
}