package com.bgaebalja.blogbackend.user.exception

class DeleteUserFailException(message: String) : RuntimeException() {
    override val message: String?
        get() = super.message
    override val cause: Throwable?
        get() = super.cause
}
