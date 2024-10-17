package com.bgaebalja.blogbackend.user.exception

import java.io.PrintStream
import java.io.PrintWriter

class JwtCustomException(s: String) : RuntimeException() {
    override fun getLocalizedMessage(): String {
        return super.getLocalizedMessage()
    }

    override fun initCause(cause: Throwable?): Throwable {
        return super.initCause(cause)
    }

    override fun printStackTrace() {
        super.printStackTrace()
    }

    override fun printStackTrace(s: PrintStream?) {
        super.printStackTrace(s)
    }

    override fun printStackTrace(s: PrintWriter?) {
        super.printStackTrace(s)
    }

    override fun fillInStackTrace(): Throwable {
        return super.fillInStackTrace()
    }

    override fun getStackTrace(): Array<StackTraceElement> {
        return super.getStackTrace()
    }

    override fun setStackTrace(stackTrace: Array<out StackTraceElement>?) {
        super.setStackTrace(stackTrace)
    }

    override val cause: Throwable?
        get() = super.cause
    override val message: String?
        get() = super.message
}