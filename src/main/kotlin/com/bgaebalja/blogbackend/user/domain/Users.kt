package com.example.kotlinprojecttest.user.domain

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GenerationType.IDENTITY
import java.util.*

@Entity
class Users(
    val email: String,
    val password: String,
    val userId: String,
    val username: String,
    val fullName: String,
    @ElementCollection(fetch = LAZY)
    @CollectionTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")]
    ) var roles: MutableList<UserRole> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null

    companion object {
        fun createUser(email: String, username: String, password: String, fullName: String): Users {
            val user = Users(
                email = email,
                password = password,
                userId = UUID.randomUUID().toString(),
                username = username,
                fullName = fullName
            )
            user.roles.add(UserRole("USER"))
            return user
        }
    }

}