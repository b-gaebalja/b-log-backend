package com.example.kotlinprojecttest.user.domain

import jakarta.persistence.Embeddable

@Embeddable
class UserRole(
    var role: String? = null
)


