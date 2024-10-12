package com.bgaebalja.blogbackend.user.repository

import com.bgaebalja.blogbackend.user.domain.Users

interface UserRepositoryCustom {

    fun findUserWithRole(email: String) : Users

}