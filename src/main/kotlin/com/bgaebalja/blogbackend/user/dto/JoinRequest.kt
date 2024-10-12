package com.example.kotlinprojecttest.user.dto

import org.springframework.web.multipart.MultipartFile

data class JoinRequest(
   var email: String,
     val password: String,
    val username: String,
    val fullName: String,
    val file: MutableList<MultipartFile> = mutableListOf()
)
