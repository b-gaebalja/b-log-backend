package com.bgaebalja.blogbackend.user.repository

import com.bgaebalja.blogbackend.user.domain.Users
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<Users, Long>, UserRepositoryCustom {
    fun findByEmailAndDeleteYn(email: String, deleteYn: Boolean): Users?

    fun findOneByUserId(userId: String): Users?
}
