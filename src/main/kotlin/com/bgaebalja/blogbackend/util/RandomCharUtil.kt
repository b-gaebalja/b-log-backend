package com.bgaebalja.blogbackend.util

fun createPassword(): String {
    val buffer = StringBuilder()
    repeat(10) {
        val randomChar = ('a'..'z').random()
        buffer.append(randomChar)
    }
    return buffer.toString()
}